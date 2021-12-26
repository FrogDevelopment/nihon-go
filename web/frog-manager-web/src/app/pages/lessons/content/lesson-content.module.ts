import {NgModule} from '@angular/core';

import {LessonContentRoutingModule} from './lesson-content-routing.module';
import {LessonContentComponent} from './lesson-content.component';
import {CommonModule} from '@angular/common';
import {LessonEditModalModule} from '../lesson-edit-modal/lesson-edit-modal.module';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzTableModule} from 'ng-zorro-antd/table';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzToolTipModule} from 'ng-zorro-antd/tooltip';
import {NzPopconfirmModule} from 'ng-zorro-antd/popconfirm';
import {NzListModule} from 'ng-zorro-antd/list';
import {NzAvatarModule} from 'ng-zorro-antd/avatar';
import {NzTagModule} from 'ng-zorro-antd/tag';
import {FormsModule} from '@angular/forms';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzMenuModule} from 'ng-zorro-antd/menu';
import {NzBreadCrumbModule} from 'ng-zorro-antd/breadcrumb';
import {NzPageHeaderModule} from 'ng-zorro-antd/page-header';


@NgModule({
    imports: [
        LessonContentRoutingModule,
        NzIconModule,
        NzButtonModule,
        NzTableModule,
        CommonModule,
        NzToolTipModule,
        NzPopconfirmModule,
        NzListModule,
        NzAvatarModule,
        NzTagModule,
        LessonEditModalModule,
        FormsModule,
        NzLayoutModule,
        NzMenuModule,
        NzBreadCrumbModule,
        NzPageHeaderModule
    ],
  exports: [LessonContentComponent],
  declarations: [LessonContentComponent]
})
export class LessonContentModule {
}
