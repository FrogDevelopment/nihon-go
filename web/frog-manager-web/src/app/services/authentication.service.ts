import {Injectable} from '@angular/core';
import {Observable, ReplaySubject} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';
import {TokenService} from './token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  constructor(private http: HttpClient,
              private router: Router,
              private tokenService: TokenService) {
  }

  login(username: string, password: string): Observable<boolean> {
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

    const body = new HttpParams()
      .set('username', `${username}`)
      .set('password', `${password}`)
      .set('client_id', 'frog-manager')
      .set('time_zone', Intl.DateTimeFormat().resolvedOptions().timeZone)
    ;

    const postObservable = this.http.post<any>(`${environment.baseUrl}/authentication/login`, body.toString(), {headers});
    const subject = new ReplaySubject<boolean>(1);
    subject.subscribe((response: any) => {
      this.tokenService.setAccessToken(response.access_token);
      this.tokenService.setRefreshToken(response.refresh_token);
      this.tokenService.setIdToken(response.id_token);
    }, (err) => {
      this.handleAuthenticationError(err);
    });

    postObservable.subscribe(subject);

    return subject;
  }

  logout() {
    // logout => remove from localStorage
    this.tokenService.resetTokens();

    this.http.post<void>(`${environment.baseUrl}/authentication/logout`, null);

    this.router.navigate(['/login']);
  }

  private handleAuthenticationError(err: any) {
    this.tokenService.resetTokens();
  }

}

