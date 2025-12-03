package com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeContentMetadata {
    private String id;
    private String title;
    private String description;
    private String thumbnailUrl;
    private String channelTitle;
    private String publishedAt;
}
