import {NgModule} from '@angular/core';
import {LessonEditModalComponent} from './lesson-edit-modal.component';
import {NzAlertModule, NzButtonModule, NzDividerModule, NzIconModule, NzSelectModule, NzToolTipModule} from 'ng-zorro-antd';
import {NzFormModule} from 'ng-zorro-antd/form';
import {CommonModule} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';

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
    ReactiveFormsModule
  ],
  exports: [LessonEditModalComponent],
  declarations: [LessonEditModalComponent]
})
export class LessonEditModalModule {
}
