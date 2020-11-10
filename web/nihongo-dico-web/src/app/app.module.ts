import {BrowserModule} from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {SearchComponent} from './search/search.component';
import {ReactiveFormsModule} from '@angular/forms';
import {SearchDetailsComponent} from './search-details/search-details.component';
import {EntriesService} from './_services/entries.service';
import {AppRoutingModule} from './app-routing.module';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatDialogModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatProgressBarModule,
  MatRadioModule,
  MatToolbarModule,
  MatTooltipModule
} from '@angular/material';
import {CustomReuseStrategy} from './custom-reuse-strategy';
import {RouteReuseStrategy} from '@angular/router';
import {AboutComponent} from './about/about.component';
import {SafeHtmlPipe} from './search/safe-html.pipe';
import {SearchDetailResolver} from './search-details/search-details-resolver.service';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {registerLocaleData} from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import localeFrExtra from '@angular/common/locales/extra/fr';
import {SearchSettingsComponent} from './search-settings/search-settings.component';
import {HttpErrorInterceptor} from './_interceptors/http-error.interceptor';
import {SentencesService} from './_services/sentences.service';

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    SearchDetailsComponent,
    AboutComponent,
    SafeHtmlPipe,
    SearchSettingsComponent
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatProgressBarModule,
    MatRadioModule,
    MatToolbarModule,
    MatTooltipModule,
    ReactiveFormsModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],

  entryComponents: [
    AboutComponent,
    SearchSettingsComponent
  ],

  providers: [
    EntriesService,
    SentencesService,
    SearchDetailResolver,
    {
      provide: RouteReuseStrategy,
      useClass: CustomReuseStrategy
    },
    {provide: LOCALE_ID, useValue: 'fr_FR'},
    {provide: HTTP_INTERCEPTORS, useClass: HttpErrorInterceptor, multi: true},
  ],

  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}

registerLocaleData(localeFr, 'fr-FR', localeFrExtra);
