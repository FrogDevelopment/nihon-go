import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuard} from './guards/auth.guard';

const appRoutes: Routes = [
  {path: 'login', loadChildren: () => import('./pages/login/login.module').then(m => m.LoginModule)},
  {path: 'lessons', canActivate: [AuthGuard], loadChildren: () => import('./pages/lessons/lessons.module').then(m => m.LessonsModule)},
  {path: 'dictionary', canActivate: [AuthGuard], loadChildren: () => import('./pages/dictionary/dictionary.module').then(m => m.DictionaryModule)},
  {path: '', redirectTo: '/lessons', pathMatch: 'full'},
  // default redirect to main page
  // otherwise redirect to the not found page
  {path: '**', loadChildren: () => import('./pages/page-not-found/page-not-found.module').then(m => m.PageNotFoundModule)}
];

@NgModule({
  imports: [
    RouterModule.forRoot(appRoutes, { relativeLinkResolution: 'legacy' })
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {
}
