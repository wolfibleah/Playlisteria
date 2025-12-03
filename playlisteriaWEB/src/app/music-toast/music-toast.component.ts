import { Component, Input } from '@angular/core';
import { YoutubeVideoMetadata } from '../models/youtube-video-metadata';
import { Router } from '@angular/router';
import { YoutubeVideosService } from '../services/youtube-videos.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SpotifySongMetadata } from '../models/spotify-song-metadata';

@Component({
  selector: 'app-music-toast',
  templateUrl: './music-toast.component.html',
  styleUrls: ['./music-toast.component.scss'],
})
export class MusicToastComponent {
  @Input() youtubeMetaData?: YoutubeVideoMetadata;
  @Input() spotifyMetaData?: SpotifySongMetadata;
  @Input() platform: string = "";
  authenticated = false;

  constructor(
    private readonly router: Router,
    private readonly youtubeVideoService: YoutubeVideosService,
    private _snackBar: MatSnackBar,
  ) {}
  ngOnInit(): void {}

  navigateToSongInfo(): void {
    if(this.platform === 'youtube')
    {
      this.router.navigate(['/songinfo'], {
        queryParams: { platform: 'youtube', metadata: JSON.stringify(this.youtubeMetaData) },
      });
    }
    else {
      this.router.navigate(['/songinfo'], {
        queryParams: { platform: 'spotify', metadata: JSON.stringify(this.spotifyMetaData) }
      })
    }
  }

  downloadSong(event: Event): void {
    this._snackBar.open('Downloading started', 'OK', {
      panelClass: ['snackbar-normal'],
      verticalPosition: 'bottom',
      horizontalPosition: 'center',
    });

    if(this.platform === 'youtube') {
      this.youtubeVideoService.getAudioAsBlobByUrl(`https://www.youtube.com/watch?v=${this.youtubeMetaData!.id}`).subscribe({
        next: (res) => {
          const url = URL.createObjectURL(res);
          const anchor = document.createElement("a");
          anchor.setAttribute("download", this.youtubeMetaData?.title! + ".mp3");
          anchor.setAttribute("href", url);
          anchor.click();
        },
        error: (err) => {
          this._snackBar.open('Eroare la descarcarea melodiei!', "OK", {
            panelClass: ['snackbar-error'],
            verticalPosition: "bottom",
            horizontalPosition: "center"
          });
        },
        complete: () => {
          this._snackBar.open('Download process complete', 'OK', {
            panelClass: ['snackbar-normal'],
          });
        }
      });
    }
    else {
      this._snackBar.open('Nu poti descarca melodii de pe Spotify')
    }
  }
}
