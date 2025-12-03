import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { YoutubeVideosService } from '../services/youtube-videos.service';
import { Router } from '@angular/router';
import { SearchBarComponent } from '../search-bar/search-bar.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  @ViewChild(SearchBarComponent) searchBar!: SearchBarComponent;
  public currentSlogan = '';
  private interval: any;
  searchUrl = '';

  constructor(
    private readonly youtubeVideoService: YoutubeVideosService,
    private router: Router,
  ) {}

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

  navigateToRegister() {
    this.router.navigate(['/register']);
  }

  ngOnInit(): void {
    this.sloganAnimation();
  }

  searchMetadata() {
    if(this.searchUrl != "") {
      this.router.navigate(['/navigate'], { queryParams: { searchedText: this.searchUrl, searchBy: this.searchBar.searchBy }});
    }
    else {
      this.router.navigate(['/navigate']);
    }
  }

  updateSearchText(value: string): void {
    this.searchUrl = value;
  }

  sloganAnimation(): void {
    var words = ['Your sound', 'Your playlist', 'Your experience'];
    var part;
    var i = 0;
    var offset = 0;
    var len = words.length;
    var forwards = true;
    var skip_count = 0;
    var skip_delay = 15;
    var speed = 50;

    this.interval = setInterval(() => {
      if (forwards) {
        if (offset >= words[i].length) {
          ++skip_count;
          if (skip_count == skip_delay) {
            forwards = false;
            skip_count = 0;
          }
        }
      } else {
        if (offset == 0) {
          forwards = true;
          i++;
          offset = 0;
          if (i >= len) {
            i = 0;
          }
        }
      }
      part = words[i].substr(0, offset);
      if (skip_count == 0) {
        if (forwards) {
          offset++;
        } else {
          offset--;
        }
      }

      this.currentSlogan = part;
    }, speed);
  }
}
