package com.echipaMisterelor.playlisteriaAPI.Services.ContentSearch;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.SpotifyContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class SpotifySearchService {

    @Value("${spotify.api.host}")
    private String apiUrl;

    private String apiKey;

    public List<SpotifyContentMetadata> getVideos(String query, int maxResults) {

        // encode the query
        try {
            query = URLEncoder.encode(query, "UTF-8");
            System.out.println("Encoded Video Name: " + query);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(java.net.URI.create(apiUrl + "search?q=" + query + "&type=track&offset=0&limit=" + maxResults))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer NTFjOWE0MThlY2ExNDRhNmI4N2I0MDFjMTY5MTFhMWY6OTZiNzJmMWNhYWVkNDZiN2I3ZjcwMjFlNWZlNjFhMDI=")
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            //if the response is 401 or the apikey is null, get a new token and retry the request
            if(response.statusCode() == 401 || apiKey == null){
                request = HttpRequest.newBuilder()
                        .uri(URI.create("https://accounts.spotify.com/api/token?grant_type=client_credentials"))
                        .header("Authorization", "Basic NTFjOWE0MThlY2ExNDRhNmI4N2I0MDFjMTY5MTFhMWY6OTZiNzJmMWNhYWVkNDZiN2I3ZjcwMjFlNWZlNjFhMDI=")
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(""))
                        .build();

                response = client.send(request, HttpResponse.BodyHandlers.ofString());

                String json = response.body();
                //extract the token
                apiKey = JsonPath.read(json, "$.access_token");

                request = HttpRequest.newBuilder()
                        .uri(java.net.URI.create(apiUrl + "search?q=" + query + "&type=track&offset=0&limit=" + maxResults))
                        .header("accept", "application/json")
                        .header("Authorization", "Bearer " + apiKey)
                        .build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }


            String json = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<String, Object> map = objectMapper.readValue(json, LinkedHashMap.class);

            List<SpotifyContentMetadata> videoList = new ArrayList<>();

            for(int i = 0; i < maxResults; i++){
                try {
                    String id = JsonPath.read(map, "$.tracks.items[" + i + "].id");
                    String title = JsonPath.read(map, "$.tracks.items[" + i + "].name");
                    String artist = JsonPath.read(map, "$.tracks.items[" + i + "].artists[0].name");
                    String thumbnailUrl = JsonPath.read(map, "$.tracks.items[" + i + "].album.images[0].url");
                    String releaseDate = JsonPath.read(map, "$.tracks.items[" + i + "].album.release_date");

                    videoList.add(new SpotifyContentMetadata(id, title, artist, thumbnailUrl, releaseDate));
                }catch (PathNotFoundException e) {
                    videoList.add(new SpotifyContentMetadata("", "Video not found", "", "", ""));
                }
            }

            return videoList;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
