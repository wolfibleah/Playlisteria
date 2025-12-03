import { Component, Input, OnInit, Pipe, SecurityContext } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { YoutubeVideosService } from '../services/youtube-videos.service';
import {
  DomSanitizer,
  SafeResourceUrl,
  SafeUrl,
} from '@angular/platform-browser';
import { YoutubeVideoMetadata } from '../models/youtube-video-metadata';
import { SpotifySongMetadata } from '../models/spotify-song-metadata';

@Component({
  selector: 'app-song-info',
  templateUrl: './song-info.component.html',
  styleUrls: ['./song-info.component.scss'],
})
export class SongInfoComponent implements OnInit {
  [x: string]: any;
  url!: SafeResourceUrl;
  youtubeMetadata!: YoutubeVideoMetadata;
  spotifyMetadata!: SpotifySongMetadata;
  protected platform = "";

  constructor(
    private readonly activatedRouted: ActivatedRoute,
    private readonly sanitizer: DomSanitizer,
  ) {}

  ngOnInit(): void {
    this.activatedRouted.queryParams.subscribe((params) => {
      this.platform = params['platform'];

      if(this.platform === 'youtube')
      {
        this.youtubeMetadata = JSON.parse(params['metadata']);
        this.youtubeMetadata.description = this.youtubeMetadata.description.trimStart();


        this.url = this.sanitizer.bypassSecurityTrustResourceUrl(
          `https://www.youtube.com/embed/${this.youtubeMetadata.id}`,
        );
      }
      else {
        this.spotifyMetadata = JSON.parse(params['metadata']);

        this.url = this.sanitizer.bypassSecurityTrustResourceUrl(
          `https://open.spotify.com/embed/track/${this.spotifyMetadata.id}`
        )
      }

      console.log(this.platform);
      
    });
  }

  extractFirstSentence(description: string | undefined): string {
    if (!description) {
      return 'no description';
    }

    const firstSentence = description.split('. ')[0];
    return firstSentence;
  }
}
