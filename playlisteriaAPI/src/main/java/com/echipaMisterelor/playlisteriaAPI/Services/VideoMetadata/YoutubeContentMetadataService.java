package com.echipaMisterelor.playlisteriaAPI.Services.VideoMetadata;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;

@Service
public class YoutubeContentMetadataService {

    @Value("${youtube.api.key}")
    private String apiKey;
    @Value("${youtube.api.host}")
    private String apiUrl;

    public YoutubeContentMetadata getContentMetadata(String videoId){

        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(apiUrl + "videos?part=snippet&id=" + videoId + "&key=" + apiKey))
                    .header("accept", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<String, Object> map = objectMapper.readValue(json, LinkedHashMap.class);

            String id = JsonPath.read(map, "$.items[0].id");
            String title = JsonPath.read(map, "$.items[0].snippet.title");
            String description = JsonPath.read(map, "$.items[0].snippet.description");
            String publishedAt = JsonPath.read(map, "$.items[0].snippet.publishedAt");
            String channelTitle = JsonPath.read(map, "$.items[0].snippet.channelTitle");
            String thumbnailUrl = JsonPath.read(map, "$.items[0].snippet.thumbnails.high.url");

            return new YoutubeContentMetadata(id, title, description, thumbnailUrl, channelTitle, publishedAt);
        }catch (IOException | InterruptedException e){
            throw new RuntimeException(e);
        }
    }

}