package com.echipaMisterelor.playlisteriaAPI.Services.VideoMetadata;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.SpotifyContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;

@Service
public class SpotifyContentMetadataService {

    @Value("${spotify.api.host}")
    private String apiUrl;

    private String apiKey;

    public SpotifyContentMetadata getContentMetadata(String videoId){

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "tracks/" + videoId))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
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
                        .uri(URI.create(apiUrl + "tracks/" + videoId))
                        .header("accept", "application/json")
                        .header("Authorization", "Bearer " + apiKey)
                        .build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            }


            String json = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<String, Object> map = objectMapper.readValue(json, LinkedHashMap.class);

            String id = JsonPath.read(map, "$.id");
            String title = JsonPath.read(map, "$.name");
            String artist = JsonPath.read(map, "$.artists[0].name");
            String releaseDate = JsonPath.read(map, "$.album.release_date");
            String thumbnailUrl = JsonPath.read(map, "$.album.images[0].url");

            return new SpotifyContentMetadata(id, title, artist, thumbnailUrl, releaseDate);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

}
