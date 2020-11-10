import {AuthenticationService} from './services/authentication.service';
import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  isCollapsed = false;

  constructor(private authService: AuthenticationService) {
  }

  logout(): void {
    this.authService.logout();
  }

  isLogged(): boolean {
    return localStorage.getItem('id_token') !== null;
  }
}
