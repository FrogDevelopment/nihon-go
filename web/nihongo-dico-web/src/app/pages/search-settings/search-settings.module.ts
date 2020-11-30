import {NgModule} from '@angular/core';
import {SearchSettingsComponent} from './search-settings.component';
import {MatDialogModule} from '@angular/material/dialog';
import {TranslateModule} from '@ngx-translate/core';
import {ReactiveFormsModule} from '@angular/forms';
import {MatRadioModule} from '@angular/material/radio';
import {MatButtonModule} from '@angular/material/button';
import {CommonModule} from '@angular/common';

@NgModule({
  imports: [
    MatDialogModule,
    TranslateModule,
    ReactiveFormsModule,
    MatRadioModule,
    MatButtonModule,
    CommonModule
  ],
  declarations: [SearchSettingsComponent],
  exports: [SearchSettingsComponent]
})
export class SearchSettingsModule {

}
