import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LessonContentComponent} from './lesson-content.component';

const routes: Routes = [
  {
    path: '',
    component: LessonContentComponent
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LessonContentRoutingModule {
}
