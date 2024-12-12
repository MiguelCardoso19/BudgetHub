import { Injectable } from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {jwtDecode} from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  tokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(localStorage.getItem('token'));

  constructor() {
    const token = localStorage.getItem('token');
    this.tokenSubject.next(token);
  }

  set token(token: string) {
    localStorage.setItem('token', token);
    this.tokenSubject.next(token);
  }

  set refreshToken(refreshToken: string) {
    localStorage.setItem('refreshToken', refreshToken);
  }

  get refreshToken() {
    return localStorage.getItem('refreshToken') as string;
  }

  get token() {
    return localStorage.getItem('token') as string;
  }

  get token$() {
    return this.tokenSubject.asObservable();
  }

  removeRefreshToken() {
    localStorage.removeItem('refreshToken');
  }

  removeToken() {
    localStorage.removeItem('token');
    this.tokenSubject.next(null);
  }

  isAuthenticated(): boolean {
    return this.tokenSubject.value !== null;
  }

  isTokenExpired(token: string): boolean {
    try {
      const decoded = jwtDecode(token);
      if (!decoded || !decoded.exp) {
        return false;
      }

      const currentTime = Date.now() / 1000;
      return decoded.exp > currentTime;
    } catch (error) {
      return false;
    }
  }

}
