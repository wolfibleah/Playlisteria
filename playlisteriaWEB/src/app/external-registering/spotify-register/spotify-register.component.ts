import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';

@Component({
  selector: 'app-spotify-register',
  templateUrl: './spotify-register.component.html',
  styleUrls: ['./spotify-register.component.scss']
})
export class SpotifyRegisterComponent implements OnInit {

  constructor(private readonly router: Router,
    private readonly cookieService: CookieService,) {}

  ngOnInit(): void {
    const params = this.router.url.split("#")[1].split('&');

    const access_token = params.filter((p) => p.includes("access_token"))[0].split("=")[1];

    this.cookieService.set('spotify_access_token', access_token);

    this.router.navigate(['navigate']);
  }
}
