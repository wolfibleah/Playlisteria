import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { YoutubeVideoMetadata } from '../models/youtube-video-metadata';
import { SpotifySongMetadata } from '../models/spotify-song-metadata';
import { PlaylistService } from '../services/playlist.service';
import { ConvertorService } from '../services/convertor.service';

@Component({
  selector: 'app-playlist-view',
  templateUrl: './playlist-view.component.html',
  styleUrls: ['./playlist-view.component.scss']
})
export class PlaylistViewComponent implements OnInit {
  protected playlistId = "";
  protected platform = "";
  protected playlistName = "";
  protected youtubePlaylist: YoutubeVideoMetadata[] = [];
  protected spotifyPlaylist: SpotifySongMetadata[] = [];

  constructor(private readonly activatedRoute: ActivatedRoute,
              private readonly playlistService: PlaylistService,
              private readonly convertorService: ConvertorService) {}

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params) => {
      this.playlistId = params['id'];
      this.platform = params['platform'];
      this.playlistName = params['name'];

      if(this.platform === 'youtube')
      {
        this.playlistService.getYoutubePlaylistData(this.playlistId).subscribe({
          next: (res) => {
            this.youtubePlaylist = res;
          }
        })
      }
      else if(this.platform === 'spotify')
      {
        this.playlistService.getSpotifyPlaylistData(this.playlistId).subscribe({
          next: (res) => {
            this.spotifyPlaylist = res;
          }
        })
      }
    })
  }

  convert(value:string) {
    if(value === 'youtube')
    {
      this.convertorService.convertToYoutube(this.playlistId, this.playlistName).subscribe(
        (res) => {
          console.log(res);
        }
      )
    }
    else {
      this.convertorService.convertToSpotify(this.playlistId, this.playlistName).subscribe(
        (res) => {
          console.log(res);
        }
      )
    }
  }
}
