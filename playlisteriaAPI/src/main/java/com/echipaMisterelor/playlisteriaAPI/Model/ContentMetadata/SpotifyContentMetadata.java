package com.echipaMisterelor.playlisteriaAPI.Model.ContentMetadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyContentMetadata {
    private String id;
    private String title;
    private String artist;
    private String thumbnailUrl;
    private String releaseDate;
}
