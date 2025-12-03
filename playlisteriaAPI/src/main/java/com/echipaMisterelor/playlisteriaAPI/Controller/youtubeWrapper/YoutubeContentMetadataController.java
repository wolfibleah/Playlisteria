package com.echipaMisterelor.playlisteriaAPI.Controller.youtubeWrapper;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
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
public class YoutubeContentMetadataController {
    @Autowired
    private YoutubeContentMetadataService youtubeContentMetadataService;

    @GetMapping("/youtubeContentMetadata/{contentId}")
    public ResponseEntity<YoutubeContentMetadata> getYoutubeContentMetadata(@PathVariable String contentId){
        return new ResponseEntity<>(youtubeContentMetadataService.getContentMetadata(contentId), HttpStatus.OK);
    }

}
