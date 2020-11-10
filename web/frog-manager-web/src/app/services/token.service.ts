import {Injectable} from '@angular/core';
import {Observable, of} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {map, share} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor(private http: HttpClient/*,
              private jwtHelperService: JwtHelperService*/) {
  }

  private refresh(): Observable<string> {
    const time_zone = Intl.DateTimeFormat().resolvedOptions().timeZone;

    return this.http.get<string>(`${environment.baseUrl}/authentication/token/refresh?time_zone=${time_zone}`)
      .pipe(
        share(), // <========== SHARE THIS OBSERVABLE TO AVOID MULTIPLE REQUEST BEING SENT SIMULTANEOUSLY
        map(newAccessToken => {
          this.setAccessToken(newAccessToken);

          return newAccessToken;
        })
      );
  }

  resetTokens() {
    this.setAccessToken(null);
    this.setRefreshToken(null);
    this.setIdToken(null);
  }

  setAccessToken(accessToken: string) {
    this.setToken('access_token', accessToken);
  }

  setRefreshToken(refreshToken: string) {
    this.setToken('refresh_token', refreshToken);
  }

  setIdToken(idToken: string) {
    this.setToken('id_token', idToken);
  }

  private setToken(key: string, token: string) {
    if (!token) {
      localStorage.removeItem(key);
    } else {
      localStorage.setItem(key, token);
    }
  }

  public getAccessToken(): Observable<string> {
    const accessToken = localStorage.getItem('access_token');
    // if (this.jwtHelperService.isTokenExpired(accessToken)) {
    //   return this.refresh();
    // } else {
    //   return of(accessToken);
    // }

    return of(accessToken);
  }

  getRefreshToken() {
    return localStorage.getItem('refresh_token');
  }

  getIdToken() {
    return localStorage.getItem('id_token');
  }
}
