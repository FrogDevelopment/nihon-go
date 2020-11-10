import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError} from 'rxjs/operators';

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

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
          }
          console.error(`Error : ${JSON.stringify(errorMessage)}`);
          return throwError(errorMessage);
        }
      )
    );
  }
}
