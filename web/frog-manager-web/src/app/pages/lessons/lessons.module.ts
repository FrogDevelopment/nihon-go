import {NgModule} from '@angular/core';

import {LessonsRoutingModule} from './lessons-routing.module';
import {LessonsComponent} from './lessons.component';
import {NzAvatarModule, NzButtonModule, NzIconModule, NzListModule, NzPopconfirmModule, NzTableModule, NzTagModule, NzToolTipModule} from 'ng-zorro-antd';
import {CommonModule} from '@angular/common';
import {LessonEditModalModule} from './lesson-edit-modal/lesson-edit-modal.module';


@NgModule({
  imports: [
    LessonsRoutingModule,
    NzButtonModule,
    NzIconModule,
    NzTableModule,
    CommonModule,
    NzToolTipModule,
    NzPopconfirmModule,
    NzListModule,
    NzAvatarModule,
    NzTagModule,
    LessonEditModalModule
  ],
  exports: [LessonsComponent],
  declarations: [LessonsComponent]
})
export class LessonsModule {
}
