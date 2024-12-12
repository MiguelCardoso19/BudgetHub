import {Component} from '@angular/core';
import {HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {TokenService} from '../../services/token/token.service';
import {AuthenticationControllerService} from '../../services/services/authentication-controller.service';
import {catchError} from 'rxjs/operators';
import {of} from 'rxjs';
import {ErrorHandlingService} from '../../services/errorHandling/error-handling.service';
import {NgForOf, NgIf} from '@angular/common';
import {IdService} from '../../services/id/id.service';
import {UserCredentialsControllerService} from '../../services/services/user-credentials-controller.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  standalone: true,
  imports: [NgForOf, NgIf],
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  nif: string = '';
  errorMsg: Array<string> = [];
  firstName: string = '';

  constructor(
    private authService: AuthenticationControllerService,
    private tokenService: TokenService,
    private router: Router,
    private errorHandlingService: ErrorHandlingService,
    private idService: IdService,
    private userCredentialsService: UserCredentialsControllerService
  ) {
  }

  ngOnInit(): void {
      this.fetchUserCredentials();
  }

  fetchUserCredentials(): void {
    this.userCredentialsService.getUserById({ id: this.idService.id }).pipe(
      catchError(error => {
        this.errorHandlingService.handleError(error);
        return of(null);
      })
    ).subscribe((userCredentials) => {
      if (userCredentials) {
        this.firstName = userCredentials.firstName;
        this.nif = userCredentials.nif;
      }
    });
  }

  logout() {
    this.errorMsg = [];

    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + this.tokenService.token,
      'Nif': this.nif
    });

    this.authService.signOut({}, undefined, headers).pipe(
      catchError(error => {
        this.errorMsg = this.errorHandlingService.handleError(error);
        return of(null);
      })
    ).subscribe(() => {
      this.tokenService.removeToken();
      this.tokenService.removeRefreshToken();
      this.idService.removeId();
      this.router.navigate(['/login']);
    });
  }

  formatText(value: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }
}
