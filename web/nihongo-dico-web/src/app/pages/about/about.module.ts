import {NgModule} from '@angular/core';
import {AboutComponent} from './about.component';
import {MatDialogModule} from '@angular/material/dialog';
import {TranslateModule} from '@ngx-translate/core';
import {MatButtonModule} from '@angular/material/button';

@NgModule({
  imports: [
    MatDialogModule,
    TranslateModule,
    MatButtonModule

  ],
  declarations: [AboutComponent],
  exports: [AboutComponent]
})
export class AboutModule {

}
