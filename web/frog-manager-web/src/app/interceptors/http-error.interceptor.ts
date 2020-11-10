import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';
import {AuthenticationService} from '../services/authentication.service';
import {UNAUTHORIZED} from 'http-status-codes';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError(error => {
          let errorMessage = {};
          if (error.error instanceof ErrorEvent) {
            // client-side error
            errorMessage = {name: error.name, message: error.error.message};
          } else if (error instanceof HttpErrorResponse) {
            // server-side error
            errorMessage = {name: error.name, code: error.status, message: error.message};
            if (error.status === UNAUTHORIZED && !error.url.endsWith('/login')) {
              this.authenticationService.logout();
            }
          }
          console.error(`Error : ${JSON.stringify(errorMessage)}`);
          return throwError(errorMessage);
        }
      )
    );
  }
}
