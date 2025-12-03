import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { PlaylistService } from '../services/playlist.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-vertical-navbar',
  templateUrl: './vertical-navbar.component.html',
  styleUrls: ['./vertical-navbar.component.scss'],
})
export class VerticalNavbarComponent implements OnInit {
  isConnectedToYoutubeAccount = false;
  isConnectedToSpotifyAccount = false;
  youtubePlaylists: { id: string, name: string  }[] = [];
  spotifyPlaylists: { id: string, name: string }[] = [];

  constructor(private readonly cookieService: CookieService,
              private readonly playlistService: PlaylistService,
              private readonly router: Router) {}

  ngOnInit(): void {
    if(this.cookieService.check('youtube_access_token'))
    {
      this.isConnectedToYoutubeAccount = true;

      this.playlistService.getYoutubePlaylists().subscribe({
        next: (allPlaylists) => {
          allPlaylists.forEach((playlist) => {
            const playlistId = playlist.substring(0, playlist.indexOf(" "));
            const playlistName = playlist.substring(playlist.indexOf(" ") + 1);
            const newPlaylist = {
              id: playlistId,
              name: playlistName
            }

            this.youtubePlaylists.push(newPlaylist);
          })
        }
      });
    }

    if(this.cookieService.check('spotify_access_token'))
    {
      this.isConnectedToSpotifyAccount = true;

      this.playlistService.getSpotifyPlaylists().subscribe({
        next: (allPlaylists) => {
          allPlaylists.forEach((playlist) => {
            const playlistId = playlist.substring(0, playlist.indexOf(" "));
            const playlistName = playlist.substring(playlist.indexOf(" ") + 1);
            const newPlaylist = {
              id: playlistId,
              name: playlistName
            }

            this.spotifyPlaylists.push(newPlaylist);
          })
        }
      })
    }
  }

  goToPlaylistPage(id: string, name: string, from: string) {
    this.router.navigate(['playlist'], { queryParams: { id: id, name: name, platform: from } });
  }
}
