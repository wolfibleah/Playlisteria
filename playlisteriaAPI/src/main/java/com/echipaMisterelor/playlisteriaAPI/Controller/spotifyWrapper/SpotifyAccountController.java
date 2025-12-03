package com.echipaMisterelor.playlisteriaAPI.Controller.spotifyWrapper;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.SpotifyContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Services.ExternalAuth.SpotifyAccountManagerService;
import com.echipaMisterelor.playlisteriaAPI.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spotifyAccount")
@CrossOrigin
public class SpotifyAccountController {

    @Autowired
    private SpotifyAccountManagerService spotifyAccountManagerService;
    @Autowired
    private UserService userService;

    @GetMapping("/getPlaylists")
    public ResponseEntity<List<String>> GetAllPlaylists(@RequestHeader String authToken, @RequestHeader String spotifyAuthToken){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        List<String> result = spotifyAccountManagerService.GetAllPlaylists(spotifyAuthToken);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/viewPlaylist")
    public ResponseEntity<List<SpotifyContentMetadata>> ViewPlaylist(
            @RequestHeader String authToken, @RequestHeader String spotifyAuthToken, @RequestParam String playlistId){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        List<SpotifyContentMetadata> result = spotifyAccountManagerService.ViewPlaylist(spotifyAuthToken, playlistId);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/addToPlaylist")
    public ResponseEntity<Boolean> AddToPlaylist(@RequestHeader String authToken, @RequestHeader String spotifyAuthToken, @RequestParam String playlistId, @RequestParam String videoId){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        spotifyAccountManagerService.AddToPlaylist(spotifyAuthToken, playlistId, videoId);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/createPlaylist")
    public ResponseEntity<String> CreatePlaylist(@RequestHeader String authToken, @RequestHeader String spotifyAuthToken, @RequestParam String playlistName){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        String newPlaylistId = spotifyAccountManagerService.CreatePlaylist(spotifyAuthToken, playlistName);

        return ResponseEntity.ok(newPlaylistId);
    }

}
