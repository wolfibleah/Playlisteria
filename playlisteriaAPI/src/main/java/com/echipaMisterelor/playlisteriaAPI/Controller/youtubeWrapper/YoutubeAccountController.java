package com.echipaMisterelor.playlisteriaAPI.Controller.youtubeWrapper;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Services.ExternalAuth.YoutubeAccountManagerService;
import com.echipaMisterelor.playlisteriaAPI.Services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/youtubeAccount")
public class YoutubeAccountController {
    @Autowired
    private YoutubeAccountManagerService youtubeAccountManagerService;
    @Autowired
    private UserService userService;


    @GetMapping("/getPlaylists")
    public ResponseEntity<List<String>> GetPlaylists(@RequestHeader String authToken, @RequestHeader String ytAuthToken){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        List<String> result = youtubeAccountManagerService.GetAllPlaylists(ytAuthToken);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/viewPlaylist")
    public ResponseEntity<List<YoutubeContentMetadata>> ViewPlaylist(
           @RequestHeader String authToken, @RequestHeader String ytAuthToken, @RequestParam String playlistId, @RequestParam boolean isPublic){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        List<YoutubeContentMetadata> result = youtubeAccountManagerService.ViewPlaylist(ytAuthToken, playlistId, isPublic);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/addToPlaylist")
    public ResponseEntity<Boolean> AddToPlaylist(@RequestHeader String authToken, @RequestHeader String ytAuthToken, @RequestParam String playlistId, @RequestParam String videoId){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        youtubeAccountManagerService.AddToPlaylist(ytAuthToken, playlistId, videoId);

        return ResponseEntity.ok(true);
    }

    @PostMapping("/createPlaylist")
    public ResponseEntity<String> CreatePlaylist(@RequestHeader String authToken, @RequestHeader String ytAuthToken, @RequestParam String playlistName){
        if(!userService.isTokenValid(authToken))
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        String playlistId = youtubeAccountManagerService.CreatePlaylist(ytAuthToken, playlistName);

        return ResponseEntity.ok(playlistId);
    }

}
