import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {TokenService} from '../../services/token/token.service';

export const guestGuard: CanActivateFn = (route, state) => {

  const tokenService = inject(TokenService);
  const router = inject(Router);

  if (tokenService.isAuthenticated()) {
    router.navigate(['dashboard']);
    return false;
  }

  return true;
};
