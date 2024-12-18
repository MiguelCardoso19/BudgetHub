import {Component, OnInit, ViewChild} from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {catchError} from 'rxjs/operators';
import {of} from 'rxjs';
import {NationalityEnum} from '../../services/enums/NationalityEnum';
import {UserGenderEnum} from '../../services/enums/UserGenderEnum';
import {UserRoleEnum} from '../../services/enums/UserRoleEnum';
import {UserCredentialsControllerService} from '../../services/services/user-credentials-controller.service';
import {StorageService} from '../../services/storage/storage.service';
import {ErrorHandlingService} from '../../services/error-handling/error-handling.service';
import {PasswordPopUpComponent} from '../password-pop-up/password-pop-up.component';
import {NgForOf, NgIf} from '@angular/common';
import {UserCredentialsDto} from '../../services/models/user-credentials-dto';
import {AuthenticationControllerService} from '../../services/services/authentication-controller.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-credentials',
  imports: [ReactiveFormsModule, FormsModule, PasswordPopUpComponent, NgForOf, NgIf],
  templateUrl: './user-credentials.component.html',
  standalone: true,
  styleUrl: './user-credentials.component.scss'
})
export class UserCredentialsComponent implements OnInit {

  @ViewChild(PasswordPopUpComponent) passwordPopUpComponent!: PasswordPopUpComponent;

  // @ts-ignore
  userCredentialsDto: UserCredentialsDto = { email: '', firstName: '', password: '', dateOfBirth: '', gender: '', lastName: '', nif: '', roles: [], nationality: '', phoneNumber: '' };
  errorMsg: Array<string> = [];
  successMsg: string = '';

  nationalities = Object.values(NationalityEnum);
  genders = Object.values(UserGenderEnum);
  roles = Object.values(UserRoleEnum);

  constructor(private userCredentialsService: UserCredentialsControllerService,
              private storageService: StorageService,
              private authService: AuthenticationControllerService,
              private router: Router,
              private errorHandlingService: ErrorHandlingService,
  ) {}

  ngOnInit(): void {
    this.fetchUserCredentials();
  }

  fetchUserCredentials(): void {
    this.userCredentialsService.getUserById({ id: this.storageService.id }).pipe(
      catchError(error => {
        this.errorMsg = this.errorHandlingService.handleError(error)
        return of(null);
      })
    ).subscribe((userCredentials) => {
      if (userCredentials) {
        this.userCredentialsDto = {
          id: userCredentials.id,
          email: userCredentials.email,
          firstName: userCredentials.firstName,
          password: '',
          dateOfBirth: userCredentials.dateOfBirth,
          gender: userCredentials.gender,
          lastName: userCredentials.lastName,
          nif: userCredentials.nif,
          roles: userCredentials.roles,
          nationality: userCredentials.nationality,
          phoneNumber: userCredentials.phoneNumber
        };
      }
    });
  }

  async save(): Promise<void> {
    const modalResult = await this.passwordPopUpComponent.openModalAsync('confirm');

    if (modalResult) {
      this.userCredentialsDto.password = this.passwordPopUpComponent.password;
      this.update();
    }
  }

  async updatePassword(): Promise<void> {
    const modalResult = await this.passwordPopUpComponent.openModalAsync('update');

    if (modalResult) {
      const { password, newPassword } = this.passwordPopUpComponent;

      this.userCredentialsDto.password = password;
      this.userCredentialsDto.newPassword = newPassword;
      this.update();
    }
  }

  async deleteAccount(): Promise<void> {
    const modalResult = await this.passwordPopUpComponent.openModalAsync('confirm');

    if (modalResult) {
      this.userCredentialsDto.password = this.passwordPopUpComponent.password;
      this.delete();
    }
  }

  update(): void {
    this.errorMsg = [];
    this.userCredentialsService.update({
      body: this.userCredentialsDto
    }).subscribe({
      next: () => {
        this.setSuccessMessage('Account settings saved successfully');
      },
      error: (err) => {
        console.log(err)
        this.errorMsg = this.errorHandlingService.handleError(err)
      }
    });
  }

  signOut() {
    this.errorMsg = [];
    this.authService.signOut({}).pipe(
      catchError(error => {
        this.errorMsg = this.errorHandlingService.handleError(error);
        return of(null);
      })
    ).subscribe(() => {
      this.router.navigate(['/login']);
    });
  }

  recoverPassword(): void {
    this.errorMsg = [];
    const email = this.userCredentialsDto.email;

    this.userCredentialsService.recoverPassword({ email }).subscribe({
      next: () => {
        this.setSuccessMessage('Recover password email sent successfully');
      },
      error: (err) => {
        this.errorMsg = this.errorHandlingService.handleError(err);
      }
    });
  }


  delete(): void {
    this.errorMsg = [];
    const deleteRequestDto = {
      id: this.userCredentialsDto.id,
      password: this.userCredentialsDto.password
    };

    this.userCredentialsService.delete({body: deleteRequestDto}).subscribe({
      next: () => {
        this.router.navigate(['login']);
      },
      error: (err) => {
        this.errorMsg = this.errorHandlingService.handleError(err);
      }
    });
  }

  formatText(value: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  setSuccessMessage(message: string): void {
    this.successMsg = message;

    setTimeout(() => {
      this.successMsg = '';
    }, 4000);
  }
}
