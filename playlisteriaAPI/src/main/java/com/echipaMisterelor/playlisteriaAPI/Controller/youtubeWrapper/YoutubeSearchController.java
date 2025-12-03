package com.echipaMisterelor.playlisteriaAPI.Controller.youtubeWrapper;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
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
public class YoutubeSearchController {
    @Autowired
    private YoutubeSearchService youtubeSearchService;

    @GetMapping("/search/youtube/{query}")
    public ResponseEntity<List<YoutubeContentMetadata>> getYoutubeSearchResults(@PathVariable String query){
        return new ResponseEntity<>(youtubeSearchService.getVideos(query, 10), HttpStatus.OK);
    }

}
