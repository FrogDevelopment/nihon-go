import {NgModule} from '@angular/core';

import {LessonsRoutingModule} from './lessons-routing.module';
import {LessonsComponent} from './lessons.component';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {CommonModule} from '@angular/common';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzMenuModule} from 'ng-zorro-antd/menu';
import {LessonContentModule} from './content/lesson-content.module';

@NgModule({
  imports: [
    LessonsRoutingModule,
    NzIconModule,
    CommonModule,
    NzLayoutModule,
    NzMenuModule,
    LessonContentModule
  ],
  exports: [LessonsComponent],
  declarations: [LessonsComponent]
})
export class LessonsModule {
}
