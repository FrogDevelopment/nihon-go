import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SearchComponent} from './search/search.component';
import {SearchDetailsComponent} from './search-details/search-details.component';
import {SearchDetailResolver} from './search-details/search-details-resolver.service';

const routes: Routes = [
  {
    path: '',
    component: SearchComponent
  },
  {
    path: 'details/:senseSeq/:lang',
    component: SearchDetailsComponent,
    resolve: {
      details: SearchDetailResolver
    }
  }
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule {
}
