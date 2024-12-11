import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  set token(token: string) {
    localStorage.setItem('token', token);
  }

  set refreshToken(refreshToken: string) {
    localStorage.setItem('refreshToken', refreshToken);
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  get refreshToken() {
    return localStorage.getItem('refreshToken') as string;
  }
}
