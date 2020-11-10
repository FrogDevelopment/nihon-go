import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';
import {Observable} from 'rxjs';
import {TokenService} from '../services/token.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router,
              private tokenService: TokenService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
    const idToken = this.tokenService.getIdToken();
    if (!idToken) {
      console.log('Not authenticated => redirecting to /login');
      this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
      return false;
    } else {
      return true;
    }
  }

}
