import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormBuilder, FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {registerLocaleData} from '@angular/common';
import fr from '@angular/common/locales/fr';
import {HttpErrorInterceptor} from './interceptors/http-error.interceptor';
import {AppRoutingModule} from './app-routing.module';
import {fr_FR, NZ_I18N} from 'ng-zorro-antd/i18n';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzMenuModule} from 'ng-zorro-antd/menu';
import {NzToolTipModule} from 'ng-zorro-antd/tooltip';
import {NzIconModule, NzIconService} from 'ng-zorro-antd/icon';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzModalService} from 'ng-zorro-antd/modal';
import {NzImageService} from 'ng-zorro-antd/image';

registerLocaleData(fr);

// export function jwtOptionsFactory(tokenService) {
//   return {
//     tokenGetter: () => {
//       return tokenService.getAccessToken();
//     },
//     whitelistedDomains: [environment.domain]
//   };
// }

@NgModule({
  imports: [
    AppRoutingModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    HttpClientModule,
    // JwtModule.forRoot({
    //   jwtOptionsProvider: {
    //     provide: JWT_OPTIONS,
    //     useFactory: jwtOptionsFactory,
    //     deps: [TokenService]
    //   }
    // }),
    NzLayoutModule,
    NzMenuModule,
    NzIconModule,
    NzToolTipModule,
  ],
  declarations: [
    AppComponent
  ],
  providers: [
    // AuthGuard,
    // AuthenticationService,
    // TokenService,
    NzMessageService,
    NzImageService,
    NzIconService,
    FormBuilder,
    NzModalService,
    {provide: NZ_I18N, useValue: fr_FR},
    {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true},
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
