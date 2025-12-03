import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SpotifySongsService {

  constructor(private readonly http: HttpClient) { }

  getSpotifySongsByString(text: String): Observable<SpotifySongsService[]> {
    return this.http.get<SpotifySongsService[]>(`http://localhost:8080/search/spotify/${text}`);
  }
}
