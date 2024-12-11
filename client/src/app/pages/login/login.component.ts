import {Component} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';
import {SignInRequestDto} from '../../services/models';
import {AuthenticationControllerService} from '../../services/services/authentication-controller.service';
import {Router} from '@angular/router';
import {TokenService} from '../../services/token/token.service';

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
    private tokenService: TokenService
  ) {
  }

  login() {
    this.errorMsg = [];
    this.authService.signIn({
      body: this.signInRequest
    }).subscribe({
      next: (res) => {
        if (res instanceof Blob) {
          res.text().then((text: string) => {
            const parsedResponse = JSON.parse(text);
            this.tokenService.token = parsedResponse.token as string;
            this.tokenService.refreshToken = parsedResponse.refreshToken as string;
            this.router.navigate(['home']);
          }).catch((err) => {
            this.errorMsg.push('Error reading the Blob response: ' + err.message);
          });
        }
      },
      error: (err) => {
        if (err.error instanceof Blob) {
          err.error.text().then((errorText: string) => {
            try {
              const parsedError = JSON.parse(errorText);
              if (parsedError) {
                for (let key in parsedError) {
                  if (parsedError.hasOwnProperty(key)) {
                    if (key !== 'status' && key !== 'errorCode') {
                      this.errorMsg.push(parsedError[key]);
                    }
                  }
                }
              }
            } catch (e) {
              this.errorMsg.push(errorText);
            }
          });
        } else {
          this.errorMsg.push(err.error);
        }
      }
    });
  }

  register(): void {
    this.router.navigate(['register'])
  }
}
