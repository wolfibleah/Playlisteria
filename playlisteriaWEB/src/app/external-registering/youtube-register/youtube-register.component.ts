import { OnInit, Component } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-youtube-register',
  templateUrl: './youtube-register.component.html',
  styleUrls: ['./youtube-register.component.scss']
})
export class YoutubeRegisterComponent implements OnInit {
  constructor(private readonly router: Router,
              private readonly cookieService: CookieService,) {}

  ngOnInit(): void {
    const params = this.router.url.split("#")[1].split('&');

    const access_token = params.filter((p) => p.includes("access_token"))[0].split("=")[1];

    this.cookieService.set('youtube_access_token', access_token);
    this.router.navigate(['navigate']);

  }
}
