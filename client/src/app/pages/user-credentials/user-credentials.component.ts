import { Component, OnInit } from '@angular/core';
import { NgForOf, NgIf } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {catchError} from 'rxjs/operators';
import {of} from 'rxjs';
import {NationalityEnum} from '../../services/enums/NationalityEnum';
import {UserGenderEnum} from '../../services/enums/UserGenderEnum';
import {UserRoleEnum} from '../../services/enums/UserRoleEnum';
import {UserCredentialsControllerService} from '../../services/services/user-credentials-controller.service';
import {StorageService} from '../../services/storage/storage.service';
import {ErrorHandlingService} from '../../services/error-handling/error-handling.service';

@Component({
  selector: 'app-user-credentials',
  imports: [NgForOf, NgIf, ReactiveFormsModule, FormsModule],
  templateUrl: './user-credentials.component.html',
  standalone: true,
  styleUrl: './user-credentials.component.scss'
})
export class UserCredentialsComponent implements OnInit {

  // @ts-ignore
  userCredentialsDto: UserCredentialsDto = { email: '', firstName: '', password: '', dateOfBirth: '', gender: '', lastName: '', nif: '', roles: [], nationality: '', phoneNumber: '' };
  errorMsg: Array<string> = [];

  nationalities = Object.values(NationalityEnum);
  genders = Object.values(UserGenderEnum);
  roles = Object.values(UserRoleEnum);

  constructor(private userCredentialsService: UserCredentialsControllerService,
              private storageService: StorageService,
              private errorHandlingService: ErrorHandlingService,
  ) {}

  ngOnInit(): void {
    this.fetchUserCredentials();
  }

  fetchUserCredentials(): void {
    this.userCredentialsService.getUserById({ id: this.storageService.id }).pipe(
      catchError(error => {
        this.errorHandlingService.handleError(error);
        return of(null);
      })
    ).subscribe((userCredentials) => {
      if (userCredentials) {
        this.userCredentialsDto = {
          email: userCredentials.email || '',
          firstName: userCredentials.firstName || '',
          password: userCredentials.password || '',
          dateOfBirth: userCredentials.dateOfBirth || '',
          gender: userCredentials.gender || '',
          lastName: userCredentials.lastName || '',
          nif: userCredentials.nif || '',
          roles: userCredentials.roles || [],
          nationality: userCredentials.nationality || '',
          phoneNumber: userCredentials.phoneNumber || ''
        };
      }
    });
  }

  formatText(value: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }
}
