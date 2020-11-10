import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormBuilder, FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {fr_FR, NZ_I18N, NzIconModule, NzLayoutModule, NzMenuModule, NzMessageService, NzModalService, NzToolTipModule} from 'ng-zorro-antd';
import {registerLocaleData} from '@angular/common';
import fr from '@angular/common/locales/fr';
import {AuthGuard} from './guards/auth.guard';
import {AuthenticationService} from './services/authentication.service';
import {HttpErrorInterceptor} from './interceptors/http-error.interceptor';
import {AppRoutingModule} from './app-routing.module';
import {JWT_OPTIONS, JwtModule} from '@auth0/angular-jwt';
import {environment} from '../environments/environment';
import {TokenService} from './services/token.service';

registerLocaleData(fr);

export function jwtOptionsFactory(tokenService) {
  return {
    tokenGetter: () => {
      return tokenService.getAccessToken();
    },
    whitelistedDomains: [environment.domain]
  };
}

@NgModule({
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    JwtModule.forRoot({
      jwtOptionsProvider: {
        provide: JWT_OPTIONS,
        useFactory: jwtOptionsFactory,
        deps: [TokenService]
      }
    }),
    NzLayoutModule,
    NzMenuModule,
    NzIconModule,
    NzToolTipModule
  ],
  declarations: [
    AppComponent
  ],
  providers: [
    AuthGuard,
    AuthenticationService,
    TokenService,
    NzMessageService,
    FormBuilder,
    NzModalService,
    {provide: NZ_I18N, useValue: fr_FR},
    {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
