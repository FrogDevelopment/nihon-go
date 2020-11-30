import {NgModule} from '@angular/core';
import {SearchDetailsComponent} from './search-details.component';
import {RouterModule, Routes} from '@angular/router';
import {SearchDetailResolver} from './search-details-resolver';

const routes: Routes = [
  {
    path: '',
    component: SearchDetailsComponent,
    resolve: {
      details: SearchDetailResolver
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: [
    SearchDetailResolver,
  ]
})
export class SearchDetailsRoutingModule {

}
