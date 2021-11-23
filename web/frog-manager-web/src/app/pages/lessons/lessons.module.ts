import {NgModule} from '@angular/core';

import {LessonsRoutingModule} from './lessons-routing.module';
import {LessonsComponent} from './lessons.component';
import {CommonModule} from '@angular/common';
import {LessonEditModalModule} from './lesson-edit-modal/lesson-edit-modal.module';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzTableModule} from 'ng-zorro-antd/table';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzToolTipModule} from 'ng-zorro-antd/tooltip';
import {NzPopconfirmModule} from 'ng-zorro-antd/popconfirm';
import {NzListModule} from 'ng-zorro-antd/list';
import {NzAvatarModule} from 'ng-zorro-antd/avatar';
import {NzTagModule} from 'ng-zorro-antd/tag';


@NgModule({
  imports: [
    LessonsRoutingModule,
    NzIconModule,
    NzButtonModule,
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
