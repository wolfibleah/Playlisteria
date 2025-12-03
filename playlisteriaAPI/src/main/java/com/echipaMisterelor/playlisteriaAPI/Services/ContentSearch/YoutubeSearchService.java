package com.echipaMisterelor.playlisteriaAPI.Services.ContentSearch;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class YoutubeSearchService {

    @Value("${youtube.api.key}")
    private String apiKey;
    @Value("${youtube.api.host}")
    private String apiUrl;

    public List<YoutubeContentMetadata> getVideos(String query, int maxResults) {

        // encode the query
        try {
            query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(apiUrl + "search?part=snippet&maxResults=" + maxResults + "&q=" + query + "&type=video&key=" + apiKey))
                    .header("accept", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

//            System.out.println("This is the response from youtube:" + json);

            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<String, Object> map = objectMapper.readValue(json, LinkedHashMap.class);

            List<YoutubeContentMetadata> videoList = new ArrayList<>();

            for(int i = 0; i < maxResults; i++){
                try {
                    String id = JsonPath.read(map, "$.items[" + i + "].id.videoId");
                    String title = JsonPath.read(map, "$.items[" + i + "].snippet.title");
                    String description = JsonPath.read(map, "$.items[" + i + "].snippet.description");
                    String publishedAt = JsonPath.read(map, "$.items[" + i + "].snippet.publishedAt");
                    String channelTitle = JsonPath.read(map, "$.items[" + i + "].snippet.channelTitle");
                    String thumbnailUrl = JsonPath.read(map, "$.items[" + i + "].snippet.thumbnails.high.url");

                    videoList.add(new YoutubeContentMetadata(id, title, description, thumbnailUrl, channelTitle, publishedAt));
                }catch (PathNotFoundException e) {
                    videoList.add(new YoutubeContentMetadata("", "Video not found", "", "", "", ""));
                }
            }

            return videoList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
