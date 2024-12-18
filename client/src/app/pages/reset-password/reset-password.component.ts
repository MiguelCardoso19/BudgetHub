import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ResetPasswordRequestDto } from '../../services/models/reset-password-request-dto';
import { UserCredentialsControllerService } from '../../services/services/user-credentials-controller.service';
import { ErrorHandlingService } from '../../services/error-handling/error-handling.service';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-reset-password',
  imports: [FormsModule, NgIf],
  templateUrl: './reset-password.component.html',
  standalone: true,
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent {
  token: string = '';
  newPassword: string = '';
  confirmNewPassword: string = '';
  errorMsg: string[] = [];
  passwordsDontMatch: boolean = false;
  passwordsEmpty: boolean = false;

  constructor(
    private router: Router,
    private userCredentialsService: UserCredentialsControllerService,
    private errorHandlingService: ErrorHandlingService
  ) {
    this.token = this.getTokenFromUrl();
  }

  private getTokenFromUrl(): string {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('token') || '';
  }

  resetPassword(): void {
    this.clearErrors();

    if (this.isFormInvalid()) return;

    const resetPasswordRequestDto: ResetPasswordRequestDto = {
      token: this.token,
      newPassword: this.newPassword
    };

    this.submitResetPassword(resetPasswordRequestDto);
  }

  private clearErrors(): void {
    this.errorMsg = [];
    this.passwordsDontMatch = false;
    this.passwordsEmpty = false;
  }

  private isFormInvalid(): boolean {
    if (!this.token || !this.newPassword || !this.confirmNewPassword) {
      this.passwordsEmpty = true;
      this.errorMsg.push('Token, new password, and confirm password are required.');
      return true;
    }

    if (this.newPassword !== this.confirmNewPassword) {
      this.passwordsDontMatch = true;
      this.errorMsg.push('New password and confirm password do not match.');
      return true;
    }

    return false;
  }

  private submitResetPassword(requestDto: ResetPasswordRequestDto): void {
    this.userCredentialsService.resetPassword({ body: requestDto }).subscribe({
      next: () => this.router.navigate(['login']),
      error: (err) => this.handleServiceError(err)
    });
  }

  private handleServiceError(err: any): void {
    this.errorMsg = this.errorHandlingService.handleError(err);
  }
}
