import {NgModule} from '@angular/core';
import {SearchDetailsComponent} from './search-details.component';
import {SentencesService} from '../../services/sentences';
import {CommonModule} from '@angular/common';
import {MatButtonModule} from '@angular/material/button';
import {MatListModule} from '@angular/material/list';
import {SafeHtmlPipeModule} from '../../pipes/safe-html/safe-html-pipe.module';
import {SearchDetailsRoutingModule} from './search-details-routing.module';
import {EntriesService} from '../../services/entries';
import {TranslateModule} from '@ngx-translate/core';

@NgModule({
  imports: [
    SearchDetailsRoutingModule,
    CommonModule,
    SafeHtmlPipeModule,
    MatButtonModule,
    MatListModule,
    TranslateModule
  ],
  declarations: [SearchDetailsComponent],
  exports: [SearchDetailsComponent],
  providers: [
    SentencesService,
    EntriesService
  ],
})
export class SearchDetailsModule {

}
