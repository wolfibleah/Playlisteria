import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { YoutubeVideoMetadata } from '../models/youtube-video-metadata';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { SpotifySongMetadata } from '../models/spotify-song-metadata';

@Injectable({
  providedIn: 'root'
})
export class PlaylistService {

  constructor(private readonly http: HttpClient,
              private readonly cookieService: CookieService) { }

  getYoutubePlaylists(): Observable<String[]> {
    const access_token = this.cookieService.get('access_token');
    const youtube_token = this.cookieService.get('youtube_access_token')

    const header = new HttpHeaders()
      .set('authToken', access_token)
      .set('ytAuthToken', youtube_token);

    return this.http.get<String[]>(`http://localhost:8080/youtubeAccount/getPlaylists`, { headers: header });

  }

  getSpotifyPlaylists(): Observable<String[]> {
    const access_token = this.cookieService.get('access_token');
    const spotify_token = this.cookieService.get('spotify_access_token')

    console.log(access_token);

    const header = new HttpHeaders()
      .set('authToken', access_token)
      .set('spotifyAuthToken', spotify_token);

    return this.http.get<String[]>(`http://localhost:8080/spotifyAccount/getPlaylists`, { headers: header });
  }

  getYoutubePlaylistData(id: string): Observable<YoutubeVideoMetadata[]> {
    const access_token = this.cookieService.get('access_token');
    const youtube_token = this.cookieService.get('youtube_access_token');

    console.log(access_token);

    const header = new HttpHeaders()
      .set('authToken', access_token)
      .set('ytAuthToken', youtube_token);


    return this.http.get<YoutubeVideoMetadata[]>(`http://localhost:8080/youtubeAccount/viewPlaylist?playlistId=${id}&isPublic=false`, {headers: header});
  }

  getSpotifyPlaylistData(id: string): Observable<SpotifySongMetadata[]> {
    const access_token = this.cookieService.get('access_token');
    const spotify_token = this.cookieService.get('spotify_access_token')

    console.log(access_token);
    const header = new HttpHeaders()
    .set('authToken', access_token)
    .set('spotifyAuthToken', spotify_token);

    return this.http.get<SpotifySongMetadata[]>(`http://localhost:8080/spotifyAccount/viewPlaylist?playlistId=${id}`, { headers: header });
  }
}
