import {NgModule} from '@angular/core';

import {LoginRoutingModule} from './login-routing.module';
import {LoginComponent} from './login.component';
import {NzFormModule} from 'ng-zorro-antd/form';
import {ReactiveFormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {NzInputModule} from 'ng-zorro-antd/input';
import {NzButtonModule} from 'ng-zorro-antd/button';
import {NzAlertModule} from 'ng-zorro-antd/alert';
import {NzIconModule} from 'ng-zorro-antd/icon';

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
