import {NgModule} from '@angular/core';
import {DictionaryComponent} from './dictionary.component';
import {CommonModule} from '@angular/common';
import {DictionaryRoutingModule} from './dictionary-routing.module';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzCardModule} from 'ng-zorro-antd/card';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzToolTipModule} from 'ng-zorro-antd/tooltip';
import {NzIconModule} from 'ng-zorro-antd/icon';


@NgModule({
    imports: [
        DictionaryRoutingModule,
        NzFormModule,
        NzCardModule,
        NzButtonModule,
        NzToolTipModule,
        NzIconModule,
        CommonModule,
        NzFormModule
    ],
  declarations: [DictionaryComponent],
  exports: [DictionaryComponent]
})
export class DictionaryModule {
}
