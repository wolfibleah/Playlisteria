import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { YoutubeVideoMetadata } from '../models/youtube-video-metadata';
import { SpotifySongMetadata } from '../models/spotify-song-metadata';

@Component({
  selector: 'app-navigation-page',
  templateUrl: './navigation-page.component.html',
  styleUrls: ['./navigation-page.component.scss'],
})
export class NavigationPageComponent implements OnInit {
  youtubeMusicList: YoutubeVideoMetadata[] = [];
  spotifyMusicList: SpotifySongMetadata[] = [];

  platform = '';
  searchedText="";
  searchBy = "youtube"

  constructor(
    private activatedRouted: ActivatedRoute ) {}

  ngOnInit(): void {
    this.activatedRouted.queryParams.subscribe(
      (params) => {
        if(params['searchedText']){
          this.searchedText = params['searchedText'];
          this.searchBy = params['searchBy']
        }
      }
    )
  }

  getValueFromSearch(newMetadata: {list: YoutubeVideoMetadata[] | SpotifySongMetadata[], platform: string}) {
    if(newMetadata.platform === 'youtube')
    {
      this.youtubeMusicList = newMetadata.list as YoutubeVideoMetadata[];
      this.platform = newMetadata.platform;
    }
    else {
      this.spotifyMusicList = newMetadata.list as SpotifySongMetadata[];
      this.platform = newMetadata.platform;
    }
  }
}
