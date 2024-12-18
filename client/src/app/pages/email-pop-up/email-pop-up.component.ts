import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {NgForOf, NgIf} from '@angular/common';
import {UserCredentialsControllerService} from '../../services/services/user-credentials-controller.service';

@Component({
  selector: 'app-email-pop-up',
  templateUrl: './email-pop-up.component.html',
  standalone: true,
  imports: [FormsModule, NgIf, NgForOf],
  styleUrls: ['./email-pop-up.component.scss']
})
export class EmailPopUpComponent {
  email: string = '';
  emailEmpty: boolean = false;
  emailInvalid: boolean = false;
  showModal: boolean = false;
  errorMsg: any[] = [];
  successMsg: any[] = [];

  constructor(private userCredentialsService: UserCredentialsControllerService) {}

  openModal() {
    this.showModal = true;
  }

  onSubmit() {
    this.emailEmpty = false;
    this.emailInvalid = false;

    if (!this.email) {
      this.emailEmpty = true;
    } else if (!this.validateEmail(this.email)) {
      this.emailInvalid = true;
    } else {
      this.handleForgotPasswordEmail(this.email);
      this.closeModal();
    }
  }

  validateEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  closeModal() {
    this.showModal = false;
    this.email = '';
    this.emailEmpty = false;
    this.emailInvalid = false;
  }

  handleForgotPasswordEmail(email: string) {
    this.errorMsg = [];
    this.successMsg = [];
    this.userCredentialsService.recoverPassword({ email }).subscribe({
      next: () => {
        this.successMsg.push('Recover password email sent successfully!');
        setTimeout(() => {
          this.successMsg = [];
        }, 3000);
      },
      error: (err) => {
        this.errorMsg.push('This email was not found');
        setTimeout(() => {
          this.errorMsg = [];
        }, 3000);
      }
    });
  }
}
