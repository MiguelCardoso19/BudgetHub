import {Routes} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {LayoutComponent} from './shared/layout/layout.component';
import {authGuard} from './core/guards/auth.guard';
import {guestGuard} from './core/guards/guest.guard';
import {UserCredentialsComponent} from './pages/user-credentials/user-credentials.component';

export const routes: Routes = [
  { path: '', component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', canActivate:[authGuard], component: DashboardComponent },
      { path: 'account', canActivate:[authGuard], component: UserCredentialsComponent },
      { path: 'login', canActivate:[guestGuard], component: LoginComponent },
      { path: 'register', canActivate:[guestGuard], component: RegisterComponent },
    ]
  }
];
