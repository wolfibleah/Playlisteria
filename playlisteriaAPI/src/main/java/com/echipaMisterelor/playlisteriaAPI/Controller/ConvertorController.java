package com.echipaMisterelor.playlisteriaAPI.Controller;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.SpotifyContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Services.ContentSearch.SpotifySearchService;
import com.echipaMisterelor.playlisteriaAPI.Services.ContentSearch.YoutubeSearchService;
import com.echipaMisterelor.playlisteriaAPI.Services.ExternalAuth.SpotifyAccountManagerService;
import com.echipaMisterelor.playlisteriaAPI.Services.ExternalAuth.YoutubeAccountManagerService;
import com.echipaMisterelor.playlisteriaAPI.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/convert")
@CrossOrigin(origins = "*")
public class ConvertorController {
    @Autowired
    private YoutubeSearchService youtubeSearchService;
    @Autowired
    private SpotifySearchService spotifySearchService;
    @Autowired
    private YoutubeAccountManagerService youtubeAccountManagerService;
    @Autowired
    private SpotifyAccountManagerService spotifyAccountManagerService;
    @Autowired
    private UserService userService;

    @PostMapping("/ytToSpotify")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> convertYoutubeToSpotify(@RequestHeader String authToken, @RequestHeader String spotifyAuthToken, @RequestHeader String ytAuthToken, @RequestParam String playlistId, @RequestParam String newPlaylistName){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        // get all video titles from the youtube playlist
        List<YoutubeContentMetadata> youtubeVideos = youtubeAccountManagerService.ViewPlaylist(ytAuthToken, playlistId, false);
        List<String> videoTitles = youtubeVideos.stream().map(YoutubeContentMetadata::getTitle).toList();

        // search for the videos on spotify
        List<SpotifyContentMetadata> finalSongList = new ArrayList<>();

        for(String title : videoTitles){
            List<SpotifyContentMetadata> spotifyVideos = spotifySearchService.getVideos(title, 1);
            if(!spotifyVideos.isEmpty())
                finalSongList.add(spotifyVideos.get(0));
        }

        // get the Ids of the songs
        List<String> songIds = finalSongList.stream().map(SpotifyContentMetadata::getId).toList();

        // add the songs to the new playlist
        String newPlaylistId = spotifyAccountManagerService.CreatePlaylist(spotifyAuthToken, newPlaylistName);
        spotifyAccountManagerService.AddToPlaylist(spotifyAuthToken, newPlaylistId, String.join(",", songIds));

        return ResponseEntity.ok(newPlaylistId);
    }

    @PostMapping("/spotifyToYt")
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> convertSpotifyToYoutube(@RequestHeader String authToken, @RequestHeader String spotifyAuthToken, @RequestHeader String ytAuthToken, @RequestParam String playlistId, @RequestParam String newPlaylistName){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        // get all video titles from the spotify playlist
        List<SpotifyContentMetadata> spotifyVideos = spotifyAccountManagerService.ViewPlaylist(spotifyAuthToken, playlistId);
        List<String> videoTitles = spotifyVideos.stream().map(SpotifyContentMetadata::getTitle).toList();

        // search for the videos on youtube
        List<YoutubeContentMetadata> finalSongList = new ArrayList<>();

        for(String title : videoTitles){
            List<YoutubeContentMetadata> youtubeVideos = youtubeSearchService.getVideos(title, 1);
            if(!youtubeVideos.isEmpty())
                finalSongList.add(youtubeVideos.get(0));
        }

        // get the Ids of the songs
        List<String> songIds = finalSongList.stream().map(YoutubeContentMetadata::getId).toList();

        // add the songs to the new playlist
        String newPlaylistId = youtubeAccountManagerService.CreatePlaylist(ytAuthToken, newPlaylistName);
        youtubeAccountManagerService.AddToPlaylist(ytAuthToken, newPlaylistId, String.join(",", songIds));

        return ResponseEntity.ok(newPlaylistId);
    }

}
