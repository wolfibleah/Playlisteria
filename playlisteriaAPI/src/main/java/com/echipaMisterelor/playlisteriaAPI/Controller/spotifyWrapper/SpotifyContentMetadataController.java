package com.echipaMisterelor.playlisteriaAPI.Controller.spotifyWrapper;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.SpotifyContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Services.VideoMetadata.SpotifyContentMetadataService;
import com.echipaMisterelor.playlisteriaAPI.Services.VideoMetadata.YoutubeContentMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class SpotifyContentMetadataController {
    @Autowired
    private SpotifyContentMetadataService spotifyContentMetadataService;

    @GetMapping("/spotifyContentMetadata/{contentId}")
    public ResponseEntity<SpotifyContentMetadata> getSpotifyContentMetadata(@PathVariable String contentId){
        return new ResponseEntity<>(spotifyContentMetadataService.getContentMetadata(contentId), HttpStatus.OK);
    }

}
