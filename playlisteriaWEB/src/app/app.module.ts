import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { SearchBarComponent } from './search-bar/search-bar.component';
import { NavigationPageComponent } from './navigation-page/navigation-page.component';
import { VerticalNavbarComponent } from './vertical-navbar/vertical-navbar.component';
import { VerticalMenuComponent } from './vertical-menu/vertical-menu.component';
import { MusicToastComponent } from './music-toast/music-toast.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { UserprofileComponent } from './userprofile/userprofile.component';
import { SongInfoComponent } from './song-info/song-info.component';
import { CookieService } from 'ngx-cookie-service';
import { YoutubeRegisterComponent } from './external-registering/youtube-register/youtube-register.component';
import { SpotifyRegisterComponent } from './external-registering/spotify-register/spotify-register.component';
import { PlaylistViewComponent } from './playlist-view/playlist-view.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBarModule } from '@angular/material/snack-bar';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    SearchBarComponent,
    NavigationPageComponent,
    VerticalNavbarComponent,
    VerticalMenuComponent,
    MusicToastComponent,
    LoginComponent,
    RegisterComponent,
    UserprofileComponent,
    SongInfoComponent,
    YoutubeRegisterComponent,
    SpotifyRegisterComponent,
    PlaylistViewComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    BrowserAnimationsModule,
  ],
  providers: [CookieService],
  bootstrap: [AppComponent],
})
export class AppModule {}
