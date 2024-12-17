import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {SignInRequestDto} from '../../services/models';
import {AuthenticationControllerService} from '../../services/services/authentication-controller.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  signInRequest: SignInRequestDto = {email: '', password: ''};
  errorMsg: Array<string> = [];

  constructor(
    private router: Router,
    private authService: AuthenticationControllerService,
  ) {}

  signIn() {
    this.errorMsg = [];
    this.authService.signIn(this.signInRequest).subscribe({
      next: () => {
        this.router.navigate(['dashboard']);
      },
      error: (err) => {
        this.errorMsg = err;
      }
    });
  }

  register(): void {
    this.router.navigate(['register'])
  }
}
