import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import {
  YoutubeVideoMetadata,
  mockYoutubeVideoMetadata,
} from '../models/youtube-video-metadata';

@Injectable({
  providedIn: 'root',
})
export class YoutubeVideosService {
  constructor(private readonly http: HttpClient) {}

  getVideoMetadataByUrl(url: string): Observable<YoutubeVideoMetadata> {
    // obtin toti parametri sub forma
    // ['v=R5GNIZP0ceE', 'list=RDR5GNIZP0ceE', 'start_radio=1']
    const qParams = url.split('?')[1].split('&');
    // iau id-ul din parametrul care contine v=
    const id = qParams.filter((x) => x.includes('v='))[0].split('=')[1];

    return this.http.get<YoutubeVideoMetadata>(
      `http://localhost:8080/youtubeContentMetadata/${id}`,
    );
  }

  ///search/youtube/{query}
  getVideoMetadataByString(text: string): Observable<YoutubeVideoMetadata[]> {
    return this.http.get<YoutubeVideoMetadata[]>(
      `http://localhost:8080/search/youtube/${text}`,
    );
  }

  getAudioAsBlobByUrl(url: string): Observable<Blob> {
    // obtin toti parametri sub forma
    // ['v=R5GNIZP0ceE', 'list=RDR5GNIZP0ceE', 'start_radio=1']
    const qParams = url.split('?')[1].split('&');
    // iau id-ul din parametrul care contine v=
    const id = qParams.filter((x) => x.includes('v='))[0].split('=')[1];

    return this.http.get<Blob>(
      `http://localhost:8080/download/youtube/mp3/${id}`,
      { responseType: 'blob' as 'json' },
    );
  }
}
