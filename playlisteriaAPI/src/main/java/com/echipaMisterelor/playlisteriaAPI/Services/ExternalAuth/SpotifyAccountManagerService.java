package com.echipaMisterelor.playlisteriaAPI.Services.ExternalAuth;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.SpotifyContentMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class SpotifyAccountManagerService implements IAccountManager {

    @Value("${spotify.api.host}")
    private String apiUrl;

    private String apiKey;

    public void AddToPlaylist(String spotifyAuthToken, String playlistId, String videoId){
        // split the videoId to get the actual id
        List<String> videoIds = List.of(videoId.split(","));
        // create the json body
        StringBuilder jsonBody = new StringBuilder("{\"uris\":[");
        for(String id : videoIds){
            jsonBody.append("\"spotify:track:").append(id).append("\",");
        }
        jsonBody.deleteCharAt(jsonBody.length() - 1);
        jsonBody.append("],\"position\":0}");

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "playlists/" + playlistId + "/tracks"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + spotifyAuthToken)
                    // add url encoded form data
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<String> GetAllPlaylists(String spotifyAuthToken) {
        List<String> playlistIds = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "me/playlists?limit=50&offset=0"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + spotifyAuthToken)
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();


            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<String, Object> map = objectMapper.readValue(json, LinkedHashMap.class);

            int totalPlaylists = JsonPath.read(map, "$.total");
            int limit = JsonPath.read(map, "$.limit");

            int maxCount = Math.min(limit, totalPlaylists) > 50 ? 50 : Math.min(limit, totalPlaylists % 50);

            for (int i = 0; i < maxCount; ++i) {
                String playlistId = JsonPath.read(map, "$.items[" + i + "].id");
                String playlistName = JsonPath.read(map, "$.items[" + i + "].name");
                System.out.println(playlistId + " " + playlistName);
                playlistIds.add(playlistId + " " + playlistName);
            }

            return playlistIds;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<SpotifyContentMetadata> ViewPlaylist(String spotifyAuthToken, String playlistId) {
        List<SpotifyContentMetadata> result = new ArrayList<>();

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "playlists/" + playlistId + "/tracks"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + spotifyAuthToken)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<String, Object> map = objectMapper.readValue(json, LinkedHashMap.class);

            int limit = JsonPath.read(map, "$.limit");
            int total = JsonPath.read(map, "$.total");
            System.out.println("limit: " + limit + " total: " + total);

            int maxCount = Math.min(limit, total) > 100 ? 100 : Math.min(limit, total % 100);

            for(int i = 0; i < maxCount; ++ i){
                String id = JsonPath.read(map, "$.items[" + i + "].track.id");
                String title = JsonPath.read(map, "$.items[" + i + "].track.name");
                String artist = JsonPath.read(map, "$.items[" + i + "].track.artists[0].name");
                String releaseDate = JsonPath.read(map, "$.items[" + i + "].track.album.release_date");
                String thumbnailUrl = JsonPath.read(map, "$.items[" + i + "].track.album.images[0].url");

                result.add(new SpotifyContentMetadata(id, title, artist, thumbnailUrl, releaseDate));
            }

            // get the next page of results
            String next = JsonPath.read(map, "$.next");

            // while there are more pages of results
            while(next != null){
                // get the next page
                request = HttpRequest.newBuilder()
                        .uri(URI.create(next))
                        .header("accept", "application/json")
                        .header("Authorization", "Bearer " + spotifyAuthToken)
                        .build();
                response = client.send(request, HttpResponse.BodyHandlers.ofString());

                json = response.body();
                map = objectMapper.readValue(json, LinkedHashMap.class);

                limit = JsonPath.read(map, "$.limit");
                total = JsonPath.read(map, "$.total");

                System.out.println("limit: " + limit + " total: " + total);
                maxCount = Math.min(limit, total) > 100 ? 100 : Math.min(limit, total % 100);

                for(int i = 0; i < maxCount; ++ i){
                    String id = JsonPath.read(map, "$.items[" + i + "].track.id");
                    String title = JsonPath.read(map, "$.items[" + i + "].track.name");
                    String artist = JsonPath.read(map, "$.items[" + i + "].track.artists[0].name");
                    String releaseDate = JsonPath.read(map, "$.items[" + i + "].track.album.release_date");
                    String thumbnailUrl = JsonPath.read(map, "$.items[" + i + "].track.album.images[0].url");

                    result.add(new SpotifyContentMetadata(id, title, artist, thumbnailUrl, releaseDate));
                }


                // get the next page of results
                next = JsonPath.read(map, "$.next");
            }

            System.out.println(result.size());

            return result;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String CreatePlaylist(String spotifyAuthToken, String playlistName){
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + "me/playlists"))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + spotifyAuthToken)
                    // add url encoded form data
                    .POST(HttpRequest.BodyPublishers.ofString("{\"name\":\"" + playlistName + "\",\"public\":false}"))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();

            // get the id of the playlist
            ObjectMapper objectMapper = new ObjectMapper();
            LinkedHashMap<String, Object> map = objectMapper.readValue(json, LinkedHashMap.class);

            return JsonPath.read(map, "$.id");

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
