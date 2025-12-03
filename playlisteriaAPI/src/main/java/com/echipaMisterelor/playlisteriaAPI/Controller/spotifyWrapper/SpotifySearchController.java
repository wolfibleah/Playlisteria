package com.echipaMisterelor.playlisteriaAPI.Controller.spotifyWrapper;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.SpotifyContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Services.ContentSearch.SpotifySearchService;
import com.echipaMisterelor.playlisteriaAPI.Services.ContentSearch.YoutubeSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class SpotifySearchController {
    @Autowired
    private SpotifySearchService spotifySearchService;

    @GetMapping("/search/spotify/{query}")
    public ResponseEntity<List<SpotifyContentMetadata>> getSpotifySearchResults(@PathVariable String query){
        return new ResponseEntity<>(spotifySearchService.getVideos(query, 10), HttpStatus.OK);
    }
}
