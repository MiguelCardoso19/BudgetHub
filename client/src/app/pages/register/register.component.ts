import { Component } from '@angular/core';
import { UserCredentialsDto } from '../../services/models/user-credentials-dto';
import {NgClass, NgForOf, NgIf} from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserCredentialsControllerService } from '../../services/services/user-credentials-controller.service';
import { NationalityEnum } from '../../services/enums/NationalityEnum';
import { UserGenderEnum } from '../../services/enums/UserGenderEnum';
import { UserRoleEnum } from '../../services/enums/UserRoleEnum';
import {ErrorHandlingService} from '../../services/errorHandling/error-handling.service';

@Component({
  selector: 'app-register',
  imports: [NgForOf, NgIf, ReactiveFormsModule, FormsModule],
  templateUrl: './register.component.html',
  standalone: true,
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  // @ts-ignore
  userCredentialsDto: UserCredentialsDto = {email: '', firstName: '', password: '', dateOfBirth: '', gender: '', lastName: '', nif: '', roles: [], nationality: '', phoneNumber: ''};

  errorMsg: Array<string> = [];

  nationalities = Object.values(NationalityEnum);
  genders = Object.values(UserGenderEnum);
  roles = Object.values(UserRoleEnum);

  constructor(
    private router: Router,
    private userCredentialsService: UserCredentialsControllerService,
  private errorHandlingService: ErrorHandlingService
  ) {}

  register() {
    this.errorMsg = [];
    this.userCredentialsService.register({
      body: this.userCredentialsDto
    }).subscribe({
      next: () => {
        this.router.navigate(['login']);
      },
      error: (err) => {
        this.errorMsg = this.errorHandlingService.handleError(err);
      }
    });
  }

  signIn() {
    this.router.navigate(['login']);
  }

  formatText(value: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }
}
