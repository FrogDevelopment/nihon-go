import {NgModule} from '@angular/core';
import {LessonEditModalComponent} from './lesson-edit-modal.component';
import {NzFormModule} from 'ng-zorro-antd/form';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import {NzSelectModule} from 'ng-zorro-antd/select';
import {NzToolTipModule} from 'ng-zorro-antd/tooltip';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzIconModule} from 'ng-zorro-antd/icon';
import {NzAlertModule} from 'ng-zorro-antd/alert';
import {NzDividerModule} from 'ng-zorro-antd/divider';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzImageModule} from 'ng-zorro-antd/image';
import {NzInputNumberModule} from 'ng-zorro-antd/input-number';

@NgModule({
  imports: [
    CommonModule,
    NzFormModule,
    NzSelectModule,
    NzButtonModule,
    NzToolTipModule,
    NzIconModule,
    NzAlertModule,
    NzDividerModule,
    ReactiveFormsModule,
    NzInputModule,
    NzImageModule,
    NzInputNumberModule
  ],
  exports: [LessonEditModalComponent],
  declarations: [LessonEditModalComponent]
})
export class LessonEditModalModule {
}
