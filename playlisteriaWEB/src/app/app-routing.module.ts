import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { NavigationPageComponent } from './navigation-page/navigation-page.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { UserprofileComponent } from './userprofile/userprofile.component';
import { YoutubeRegisterComponent } from './external-registering/youtube-register/youtube-register.component';
import { SpotifyRegisterComponent } from './external-registering/spotify-register/spotify-register.component';
import { PlaylistViewComponent } from './playlist-view/playlist-view.component';
import { SongInfoComponent } from './song-info/song-info.component';

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
  },
  {
    path: 'navigate',
    component: NavigationPageComponent,
  },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'userprofile', component: UserprofileComponent },
  { path: 'songinfo', component: SongInfoComponent },
  { path: 'youtubeLogin', component: YoutubeRegisterComponent },
  { path: 'spotifyLogin', component: SpotifyRegisterComponent },
  { path: 'playlist', component: PlaylistViewComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
