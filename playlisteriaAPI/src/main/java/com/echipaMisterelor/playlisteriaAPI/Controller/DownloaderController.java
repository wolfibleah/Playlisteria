package com.echipaMisterelor.playlisteriaAPI.Controller;

import com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata.YoutubeContentMetadata;
import com.echipaMisterelor.playlisteriaAPI.Services.Downloader.DownloaderService;
import com.echipaMisterelor.playlisteriaAPI.Services.VideoMetadata.YoutubeContentMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin

public class DownloaderController {
    @Autowired
    private DownloaderService downloaderService;
    @Autowired
    private YoutubeContentMetadataService youtubeContentMetadataService;

    @GetMapping("/download/youtube/{targetFormat}/{url}")
    public ResponseEntity<Resource> downloadYoutube(@PathVariable String targetFormat, @PathVariable String url) throws IOException {
        System.out.println("Getting metadata");
        YoutubeContentMetadata youtubeContentMetadata = youtubeContentMetadataService.getContentMetadata(url);

        //check if the video was found
        if (youtubeContentMetadata.getTitle().equals("Video not found")) {
            return ResponseEntity.notFound().build();
        }

        //check if video is available already
        Path filePath = Paths.get("my_videos").resolve(youtubeContentMetadata.getTitle() + "." + targetFormat);
        Resource resource = new UrlResource(filePath.toUri());
        //if it exists, return it
        if (resource.exists() && resource.isReadable()) {
            System.out.println("Video is cached already");
            System.out.println("Sending to user");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }
        //else download it
        else {
            System.out.println("Downloading video");
            downloaderService.downloadYoutube(targetFormat, url, youtubeContentMetadata.getTitle());
            System.out.println("Sending to user");
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        }
    }


}
