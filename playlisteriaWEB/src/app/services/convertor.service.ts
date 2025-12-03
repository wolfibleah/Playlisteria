import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConvertorService {

  constructor(private readonly http: HttpClient,
              private readonly cookieService: CookieService) { }

  convertToYoutube(id: string, name: string): Observable<string> {
    const access_token = this.cookieService.get('access_token');
    const youtube_token = this.cookieService.get('youtube_access_token')
    const spotify_token = this.cookieService.get('spotify_access_token')

    const header = new HttpHeaders()
      .set('authToken', access_token)
      .set('spotifyAuthToken', spotify_token)
      .set('ytAuthToken', youtube_token);

    return this.http.post<string>(`http://localhost:8080/convert/spotifyToYt?playlistId=${id}&newPlaylistName=${name}`, {}, {headers: header});
  }

  convertToSpotify(id: string, name: string): Observable<string> {
    const access_token = this.cookieService.get('access_token');
    const youtube_token = this.cookieService.get('youtube_access_token')
    const spotify_token = this.cookieService.get('spotify_access_token')

    const header = new HttpHeaders()
      .set('authToken', access_token)
      .set('spotifyAuthToken', spotify_token)
      .set('ytAuthToken', youtube_token);

    return this.http.post<string>(`http://localhost:8080/convert/ytToSpotify?playlistId=${id}&newPlaylistName=${name}`, {}, {headers: header});

  }
}
