import {NgModule} from '@angular/core';
import {PageNotFoundRoutingModule} from './page-not-found-routing.module';
import {PageNotFoundComponent} from './page-not-found.component';
import {NzResultModule} from 'ng-zorro-antd/result';
import {NzButtonModule} from 'ng-zorro-antd/button';

@NgModule({
  imports: [
    PageNotFoundRoutingModule,
    NzResultModule,
    NzButtonModule
  ],
  declarations: [PageNotFoundComponent],
  exports: [PageNotFoundComponent]
})
export class PageNotFoundModule {
}
