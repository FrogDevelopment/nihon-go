import {BrowserModule} from '@angular/platform-browser';
import {LOCALE_ID, NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {SearchComponent} from './pages/search/search.component';
import {ReactiveFormsModule} from '@angular/forms';
import {SearchDetailsComponent} from './pages/search-details/search-details.component';
import {AppRoutingModule} from './app-routing.module';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule} from '@angular/material/button';
import {MatDialogModule} from '@angular/material/dialog';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatListModule} from '@angular/material/list';
import {MatMenuModule} from '@angular/material/menu';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatRadioModule} from '@angular/material/radio';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatTooltipModule} from '@angular/material/tooltip';
import {CustomReuseStrategy} from './custom-reuse-strategy';
import {RouteReuseStrategy} from '@angular/router';
import {AboutComponent} from './pages/about/about.component';
import {SafeHtmlPipe} from './pipes/safe-html.pipe';
import {SearchDetailResolver} from './pages/search-details/search-details-resolver.service';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {registerLocaleData} from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import localeFrExtra from '@angular/common/locales/extra/fr';
import {SearchSettingsComponent} from './pages/search-settings/search-settings.component';
import {HttpErrorInterceptor} from './interceptors';
import {EntriesService} from './services/entries';
import {SentencesService} from './services/sentences';

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
