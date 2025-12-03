import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { YoutubeVideosService } from '../services/youtube-videos.service';
import { Observable, Subject, Subscription, debounceTime, distinctUntilChanged, map, of, switchMap, takeUntil, tap, throwError } from 'rxjs';
import { YoutubeVideoMetadata } from '../models/youtube-video-metadata';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SpotifySongsService } from '../services/spotify-songs.service';
import { SpotifySongMetadata } from '../models/spotify-song-metadata';

@Component({
  selector: 'app-search',
  templateUrl: './search-bar.component.html',
  styleUrls: ['./search-bar.component.scss']
})
export class SearchBarComponent implements OnInit, OnDestroy{
  @Input() searchBarText!: string;
  @Input() searchedText!: string;
  @Output() getSearchText = new EventEmitter<string>();
  @Output() searchUrlChange = new EventEmitter<{ list: YoutubeVideoMetadata[] | SpotifySongMetadata[], platform: string}>();
  youtubeMusicList: YoutubeVideoMetadata[] = [];
  spotifyMusicList: SpotifySongMetadata[] = [];
  @Input() searchBy = 'youtube';

  /* Observables and subjects */
  private destroy$ = new Subject<void>();
  searchText$ = new Subject<string>();

  constructor(private readonly youtubeVideosService: YoutubeVideosService,
              private readonly spotifySongService: SpotifySongsService,
              private readonly _snackBar: MatSnackBar) {}

  private getAudioBase64$ = this.searchText$.pipe(
    debounceTime(500),
    distinctUntilChanged(),
    takeUntil(this.destroy$),
    switchMap((resp) => {
      if (resp === "") {
        return of([]);
      }
      else if(resp.includes("https://") || resp.includes("?v=")) {
        if(this.searchBy === 'youtube')
        {
          return this.youtubeVideosService.getVideoMetadataByUrl(resp);
        }
        else {
          this._snackBar.open("Cannot search a link for spotify. Please select Youtube before searching.", "OK");
          return of([]);
        }
      }
      else {
        if(this.searchBy === 'youtube') {
          return this.youtubeVideosService.getVideoMetadataByString(resp);
        }
        else {
          return this.spotifySongService.getSpotifySongsByString(resp);
        }
      }
    })
  )

  ngOnInit(): void {
    this.getAudioBase64$.subscribe({
      next: (resp) => {
        if(this.searchBy === 'youtube')
        {
          if(Array.isArray(resp))
          {
            this.youtubeMusicList = (resp as unknown as YoutubeVideoMetadata[]);
            this.searchUrlChange.emit({list: this.youtubeMusicList, platform: 'youtube'});

          }
          else {
            this.youtubeMusicList = [ resp ];
            this.searchUrlChange.emit({list: this.youtubeMusicList, platform: 'youtube'});
          }
        }
        else if(this.searchBy === 'spotify') {
          this.spotifyMusicList = (resp as unknown as SpotifySongMetadata[]);
          this.searchUrlChange.emit({list: this.spotifyMusicList, platform: 'spotify'})
        }
      }
    });

    if(this.searchedText != "")
    {
      this.searchBarText = this.searchedText;
      this.sendUrl();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.unsubscribe();
  }

  sendUrl(): void {
      this.searchText$.next(this.searchBarText);
      this.getSearchText.emit(this.searchBarText)
  }

  updateSearchBy(platform: string): void {
    this.searchBy = platform;
  }
}
