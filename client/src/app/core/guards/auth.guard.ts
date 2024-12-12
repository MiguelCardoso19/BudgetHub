import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';
import {TokenService} from '../../services/token/token.service';

export const authGuard: CanActivateFn = (route, state) => {

  const tokenService = inject(TokenService);
  const router = inject(Router);

  if (!tokenService.isAuthenticated()) {
    router.navigate(['login']);
    return false;
  }

  return true;
};
