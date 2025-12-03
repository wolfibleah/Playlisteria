import { Injectable } from '@angular/core';
import { SignResponse, User, UserToken } from '../models/user';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, tap } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import * as jwt_decode from 'jwt-decode';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private readonly http: HttpClient,
    private readonly cookieService: CookieService,
  ) {}

  get isAuthenticated(): string {
    return this.cookieService.get('isAuthenticated');
  }

  login(credentials: User): Observable<SignResponse> {
    return this.http
      .post<SignResponse>('http://localhost:8080/users/login', credentials)
      .pipe(
        tap((res) => {
          if (res !== null) {
            const decodedData: UserToken = jwt_decode.jwtDecode(res.token);

            this.cookieService.set('current_user', JSON.stringify(credentials));
            this.cookieService.set("isAuthenticated", "true");

            this.cookieService.set('access_token', res.token);
          }
        }),
        catchError((error) => {
          console.error('Eroare la autentificare:', error);
          throw error;
        }),
      );
  }

  register(credentials: User): Observable<SignResponse> {
    return this.http
      .post<SignResponse>('http://localhost:8080/users/register', credentials)
      .pipe(
        tap((res) => {
          if (res !== null) {
            const decodedData: UserToken = jwt_decode.jwtDecode(res.token);
            this.cookieService.set('current_user', JSON.stringify(credentials));
            this.cookieService.set("isAuthenticated", "true");

            this.cookieService.set('access_token', res.token);
          }
        }),
      );
  }
}
