import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgClass, NgIf } from '@angular/common';

@Component({
  selector: 'app-password-pop-up',
  imports: [FormsModule, NgClass, NgIf],
  templateUrl: './password-pop-up.component.html',
  standalone: true,
  styleUrl: './password-pop-up.component.scss',
})
export class PasswordPopUpComponent {
  showModal: boolean = false;
  password: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  confirmNewPassword: string = '';
  passwordsDontMatch: boolean = false;
  passwordsEmpty: boolean = false;
  mode: 'update' | 'confirm' = 'confirm';

  private resolvePromise!: (result: boolean) => void;

  openModalAsync(mode: 'update' | 'confirm' = 'confirm'): Promise<boolean> {
    this.mode = mode;
    this.showModal = true;
    return new Promise((resolve) => {
      this.resolvePromise = resolve;
    });
  }

  closeModal(): void {
    this.showModal = false;
    setTimeout(() => {
      this.resetForm();
    }, 0);
  }

  resetForm(): void {
    this.password = '';
    this.newPassword = '';
    this.confirmPassword = '';
    this.confirmNewPassword = '';
    this.passwordsDontMatch = false;
    this.passwordsEmpty = false;
  }

  onSubmitPassword(): void {
    if (this.isFormEmpty()) {
      this.passwordsEmpty = true;
      this.passwordsDontMatch = false;
      return;
    }

    if (this.mode === 'update') {
      this.handleUpdatePassword();
    } else if (this.mode === 'confirm') {
      this.handleConfirmPassword();
    }
  }

  isFormEmpty(): boolean {
    if (this.mode === 'update') {
      return !this.password || !this.newPassword || !this.confirmNewPassword;
    } else {
      return !this.password || !this.confirmPassword;
    }
  }

  handleUpdatePassword(): void {
    if (this.newPassword !== this.confirmNewPassword) {
      this.passwordsDontMatch = true;
    } else {
      this.passwordsDontMatch = false;
      this.passwordsEmpty = false;
      this.resolvePromise(true);
      this.closeModal();
    }
  }

  handleConfirmPassword(): void {
    if (this.password !== this.confirmPassword) {
      this.passwordsDontMatch = true;
    } else {
      this.passwordsDontMatch = false;
      this.passwordsEmpty = false;
      this.resolvePromise(true);
      this.closeModal();
    }
  }

  checkPasswordsMatch(): void {
    if (this.isFormEmpty()) {
      this.passwordsEmpty = true;
      this.passwordsDontMatch = false;
    } else if (this.mode === 'update') {
      this.passwordsDontMatch = this.newPassword !== this.confirmNewPassword;
      this.passwordsEmpty = false;
    } else if (this.mode === 'confirm') {
      this.passwordsDontMatch = this.password !== this.confirmPassword;
      this.passwordsEmpty = false;
    }
  }
}
