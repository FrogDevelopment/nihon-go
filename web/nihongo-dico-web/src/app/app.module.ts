import {BrowserModule} from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app-routing.module';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {registerLocaleData} from '@angular/common';
import {HttpErrorInterceptor} from './interceptors';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {AboutModule} from './pages/about/about.module';
import {MatIconModule} from '@angular/material/icon';
import en from '@angular/common/locales/en';
import {RouteReuseStrategy} from '@angular/router';
import {CustomRouteReuseStrategy} from './router/custom-route-reuse-strategy';

registerLocaleData(en);

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

@NgModule({
  declarations: [
    AppComponent
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    TranslateModule.forRoot({
      defaultLanguage: 'en',
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    }),
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    AboutModule
  ],
  providers: [
    {
      provide: RouteReuseStrategy,
      useClass: CustomRouteReuseStrategy
    },
    {
      provide: LOCALE_ID,
      useValue: 'en_US'
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor, multi: true
    },
  ],
  exports: [],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
