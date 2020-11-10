import {NgModule} from '@angular/core';

import {LoginRoutingModule} from './login-routing.module';
import {LoginComponent} from './login.component';
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzAlertModule, NzButtonModule, NzIconModule, NzInputModule} from 'ng-zorro-antd';
import {ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';


@NgModule({
  imports: [
    LoginRoutingModule,
    NzFormModule,
    NzInputModule,
    NzButtonModule,
    NzAlertModule,
    ReactiveFormsModule,
    NzIconModule,
    CommonModule
  ],
  exports: [LoginComponent],
  declarations: [LoginComponent]
})
export class LoginModule {
}
