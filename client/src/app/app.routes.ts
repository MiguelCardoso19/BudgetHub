import {Routes} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {RegisterComponent} from './pages/register/register.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {LayoutComponent} from './shared/layout/layout.component';
import {authGuard} from './core/guards/auth.guard';
import {guestGuard} from './core/guards/guest.guard';
import {UserCredentialsComponent} from './pages/user-credentials/user-credentials.component';
import {ResetPasswordComponent} from './pages/reset-password/reset-password.component';
import {BudgetTypeComponent} from './pages/budget-type/budget-type.component';
import {BudgetSubtypeComponent} from './pages/budget-subtype/budget-subtype.component';
import {MovementsComponent} from './pages/movements/movements.component';
import {InvoicesComponent} from './pages/invoices/invoices.component';
import {PaymentsComponent} from './pages/payments/payments.component';

export const routes: Routes = [
  { path: '', component: LayoutComponent,
    children: [
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
      { path: 'dashboard', canActivate:[authGuard], component: DashboardComponent },
      { path: 'account', canActivate:[authGuard], component: UserCredentialsComponent },
      { path: 'login', canActivate:[guestGuard], component: LoginComponent },
      { path: 'register', canActivate:[guestGuard], component: RegisterComponent },
      { path: 'reset-password', component: ResetPasswordComponent },
      { path: 'budget-type', canActivate:[authGuard], component: BudgetTypeComponent },
      { path: 'budget-subtype', canActivate:[authGuard], component: BudgetSubtypeComponent },
      { path: 'movements', canActivate:[authGuard], component: MovementsComponent },
      { path: 'invoices', canActivate:[authGuard], component: InvoicesComponent },
      { path: 'payments', canActivate:[authGuard], component: PaymentsComponent },
    ]
  }
];
