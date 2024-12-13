import {Component} from '@angular/core';
import {HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {TokenService} from '../../services/token/token.service';
import {AuthenticationControllerService} from '../../services/services/authentication-controller.service';
import {catchError} from 'rxjs/operators';
import {of} from 'rxjs';
import {ErrorHandlingService} from '../../services/errorHandling/error-handling.service';
import {NgForOf, NgIf} from '@angular/common';
import {StorageService} from '../../services/storage/storage.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  standalone: true,
  imports: [NgForOf, NgIf],
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  errorMsg: Array<string> = [];
  firstName: string = '';

  constructor(
    private authService: AuthenticationControllerService,
    private tokenService: TokenService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService,
    private storageService: StorageService,
  ) {
    this.firstName =  this.getNameFromStorage();
  }

  logout() {
    this.errorMsg = [];

    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.tokenService.token,
      'Nif': this.storageService.getNif() as string
    });

    this.authService.signOut({}, undefined, headers).pipe(
      catchError(error => {
        this.errorMsg = this.errorHandlingService.handleError(error);
        return of(null);
      })
    ).subscribe(() => {
      this.tokenService.removeToken();
      this.tokenService.removeRefreshToken();
      this.storageService.removeId();
      this.storageService.removeName();
      this.storageService.removeNif();
      this.router.navigate(['/login']);
    });
  }

  formatText(value: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  private getNameFromStorage(): string {
    return this.storageService.getName() as string;
  }
}
