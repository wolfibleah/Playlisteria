package com.echipaMisterelor.playlisteriaAPI.Services.ExternalAuth;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class YoutubeAccountManagerService implements IAccountManager{

    @Value("${youtube.api.key}")
    private String apiKey;
    @Value("${youtube.api.host}")
    private String apiUrl;

    public void AddToPlaylist(String ytAuthToken, String playlistId, String videoId){
        // split the videoIds by comma
        List<String> videoIds = List.of(videoId.split(","));

        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(ytAuthToken);
            YouTube youtube = new YouTube.Builder(
                    new NetHttpTransport(), new GsonFactory(), credential)
                    .setApplicationName("Playlisteria")
                    .build();

            for(String id : videoIds){

                // create the playlist item
                PlaylistItem playlistItem = new PlaylistItem();
                PlaylistItemSnippet snippet = new PlaylistItemSnippet();
                snippet.setPlaylistId(playlistId);
                ResourceId resourceId = new ResourceId();
                resourceId.setKind("youtube#video");
                resourceId.setVideoId(id);
                snippet.setResourceId(resourceId);
                playlistItem.setSnippet(snippet);

                // add the playlist item
                youtube.playlistItems().insert(List.of("snippet"), playlistItem).execute();
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<YoutubeContentMetadata> ViewPlaylist(String ytAuthToken, String playlistId, boolean isPublic){
        List<YoutubeContentMetadata> result = new ArrayList<>();

        try {
            GoogleCredential credential;
            if(!isPublic)
                credential = new GoogleCredential().setAccessToken(ytAuthToken);
            else
                credential = new GoogleCredential().setAccessToken(apiKey);

            // build the youtube object
            YouTube youtube = new YouTube.Builder(
                    new NetHttpTransport(), new GsonFactory(), credential)
                    .setApplicationName("Playlisteria")
                    .build();

            // get the playlist items
            YouTube.PlaylistItems.List playlistItemRequest = youtube.playlistItems().list(List.of("snippet","contentDetails"));
            playlistItemRequest.setKey(apiKey);
            playlistItemRequest.setPlaylistId(playlistId);
            PlaylistItemListResponse playlistItemListResponse = playlistItemRequest.execute();
            List<PlaylistItem> playlistItems = playlistItemListResponse.getItems();

            for(PlaylistItem item : playlistItems){
                // get the id, title, description, thumbnailUrl, channelTitle, publishedAt
                String id = item.getSnippet().getResourceId().getVideoId();
                String title = item.getSnippet().getTitle();
                String description = item.getSnippet().getDescription();
                String publishedAt = item.getSnippet().getPublishedAt().toString();
                String channelTitle = item.getSnippet().getChannelTitle();
                String thumbnailUrl = item.getSnippet().getThumbnails().getHigh().getUrl();

                result.add(new YoutubeContentMetadata(id, title, description, thumbnailUrl, channelTitle, publishedAt));
            }

            return result;

        }catch (Exception e){
            throw new RuntimeException(e);
        }


    }

    public List<String> GetAllPlaylists(String ytAuthToken){
        List<String> result = new ArrayList<>();

        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(ytAuthToken);
            YouTube youtube = new YouTube.Builder(
                    new NetHttpTransport(), new GsonFactory(), credential)
                    .setApplicationName("Playlisteria")
                    .build();

            // get the playlist items
            YouTube.Playlists.List playlistRequest = youtube.playlists().list(List.of("snippet"));
            playlistRequest.setKey(apiKey);
            playlistRequest.setMine(true);
            PlaylistListResponse playlistListResponse = playlistRequest.execute();
            List<Playlist> playlists = playlistListResponse.getItems();

            for(Playlist item : playlists){
                // get the id, title, description, thumbnailUrl, channelTitle, publishedAt
                String id = item.getId();
                String title = item.getSnippet().getTitle();

                result.add(id + " " + title);
            }

            return result;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public String CreatePlaylist(String ytAuthToken, String playlistName){
        try {
            GoogleCredential credential = new GoogleCredential().setAccessToken(ytAuthToken);
            YouTube youtube = new YouTube.Builder(
                    new NetHttpTransport(), new GsonFactory(), credential)
                    .setApplicationName("Playlisteria")
                    .build();

            Playlist playlist = new Playlist();
            PlaylistSnippet snippet = new PlaylistSnippet();
            snippet.setTitle(playlistName);
            playlist.setSnippet(snippet);

            Playlist createdPlaylist = youtube.playlists().insert(List.of("snippet"), playlist).execute();
            return createdPlaylist.getId();

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
