import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { CookieService } from 'ngx-cookie-service';
@Component({
  selector: 'app-vertical-menu',
  templateUrl: './vertical-menu.component.html',
  styleUrls: ['./vertical-menu.component.scss'],
})
export class VerticalMenuComponent implements OnInit {
  protected isAuthenticated = false;
  protected userName = "";
  showDropdown = false;

  isConnectedToYoutube = false;
  isConenctedToSpotify = false;

  protected readonly youtubeLoginLink = "https://accounts.google.com/o/oauth2/v2/auth?scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fyoutube&include_granted_scopes=true&state=state_parameter_passthrough_value&redirect_uri=http%3A%2F%2Flocalhost%3A4200%2FyoutubeLogin&response_type=token&client_id=46526941555-rakhlqksc1i6cb74eg9o4oue356ir01b.apps.googleusercontent.com";
  protected readonly spotifyLoginLink = "https://accounts.spotify.com/authorize?response_type=token&client_id=51c9a418eca144a6b87b401c16911a1f&scope=playlist-read-private playlist-modify-public playlist-modify-private&redirect_uri=http%3A%2F%2Flocalhost%3A4200%2FspotifyLogin"


  constructor(private readonly cookieService: CookieService) {}

  ngOnInit(): void {
    if(this.cookieService.check('isAuthenticated'))
    {
      this.isAuthenticated = this.cookieService.get('isAuthenticated') === "false" ? false : true;
      this.userName = JSON.parse(this.cookieService.get('current_user')).username;
    }

    if(this.cookieService.check('youtube_access_token'))
    {
      this.isConnectedToYoutube = true;
    }

    if(this.cookieService.check('spotify_access_token'))
    {
      this.isConenctedToSpotify = true;
    }
  }

  toggleDropdown() {
    this.showDropdown = this.showDropdown ? false : true;
  }

  logout() {
    this.cookieService.deleteAll();
  }
}
