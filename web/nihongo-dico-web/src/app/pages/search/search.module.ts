import {NgModule} from '@angular/core';
import {SearchComponent} from './search.component';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {MatInputModule} from '@angular/material/input';
import {MatListModule} from '@angular/material/list';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatTooltipModule} from '@angular/material/tooltip';
import {ReactiveFormsModule} from '@angular/forms';
import {EntriesService} from '../../services/entries';
import {MatFormFieldModule} from '@angular/material/form-field';
import {CommonModule} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import {SafeHtmlPipeModule} from '../../pipes/safe-html/safe-html-pipe.module';
import {SearchSettingsModule} from '../search-settings/search-settings.module';
import {SearchRoutingModule} from './search-routing.module';

@NgModule({
  imports: [
    SearchRoutingModule,
    SearchSettingsModule,
    CommonModule,
    ReactiveFormsModule,
    TranslateModule,
    SafeHtmlPipeModule,
    MatProgressBarModule,
    MatFormFieldModule,
    MatIconModule,
    MatInputModule,
    MatButtonModule,
    MatTooltipModule,
    MatListModule
  ],
  declarations: [SearchComponent],
  exports: [SearchComponent],
  providers: [
    EntriesService
  ]
})
export class SearchModule {

}
