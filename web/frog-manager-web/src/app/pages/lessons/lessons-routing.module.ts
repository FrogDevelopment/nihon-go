import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LessonsComponent} from './lessons.component';

const routes: Routes = [
  {
    path: '',
    component: LessonsComponent,
    children: [
      {
        path: ':id',
        loadChildren: () => import('./content/lesson-content.module').then(m => m.LessonContentModule)
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LessonsRoutingModule {
}
