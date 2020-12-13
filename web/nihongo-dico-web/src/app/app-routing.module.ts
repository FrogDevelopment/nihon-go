import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./pages/search/search.module').then(m => m.SearchModule),
  },
  {
    path: 'details/:senseSeq/:lang',
    loadChildren: () => import('./pages/search-details/search-details.module').then(m => m.SearchDetailsModule)
  }
];

@NgModule({
  exports: [RouterModule],
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule {
}
