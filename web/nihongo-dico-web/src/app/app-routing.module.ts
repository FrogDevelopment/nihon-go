import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SearchComponent} from './pages/search/search.component';
import {SearchDetailsComponent} from './pages/search-details/search-details.component';
import {SearchDetailResolver} from './pages/search-details/search-details-resolver.service';

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
  imports: [RouterModule.forRoot(routes, {relativeLinkResolution: 'legacy'})]
})
export class AppRoutingModule {
}
