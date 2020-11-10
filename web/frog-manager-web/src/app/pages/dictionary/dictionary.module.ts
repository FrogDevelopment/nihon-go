import {NgModule} from '@angular/core';
import {DictionaryComponent} from './dictionary.component';
import {NzButtonModule, NzCardModule, NzFormModule, NzIconModule, NzToolTipModule} from 'ng-zorro-antd';
import {CommonModule} from '@angular/common';
import {DictionaryRoutingModule} from './dictionary-routing.module';


@NgModule({
  imports: [
    DictionaryRoutingModule,
    NzFormModule,
    NzCardModule,
    NzButtonModule,
    NzToolTipModule,
    NzIconModule,
    CommonModule
  ],
  declarations: [DictionaryComponent],
  exports: [DictionaryComponent]
})
export class DictionaryModule {
}
