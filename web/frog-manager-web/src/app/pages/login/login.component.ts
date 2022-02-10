import {OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {AuthenticationService} from '../../services/authentication.service';
import {first} from 'rxjs/operators';

@Singleton({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;

  loading = false;
  errorMessage: string;
  returnUrl: string;

  constructor(private fb: FormBuilder,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthenticationService) {
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      userName: [null, [Validators.required]],
      password: [null, [Validators.required]]
    });

    // reset login status
    this.authService.logout();

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  login(): void {
    for (const i in this.loginForm.controls) {
      this.loginForm.controls[i].markAsDirty();
      this.loginForm.controls[i].updateValueAndValidity();
    }

    // stop here if form is invalid
    if (this.loginForm.invalid) {
      return;
    }

    const userName = this.loginForm.get('userName').value;
    const password = this.loginForm.get('password').value; // fixme hash/encrypt password
    this.loading = true;
    this.authService.login(userName, password)
      .pipe(first())
      .subscribe(
        (success) => {
          this.loading = false;
          if (success) {
            this.router.navigate([this.returnUrl]);
          } else {
            this.errorMessage = 'Incorrect credentials !';
          }
        },
        () => {
          this.errorMessage = 'Incorrect credentials !';
          this.loading = false;
        });
  }

  afterErrorClose(): void {
    this.errorMessage = undefined;
  }

}
