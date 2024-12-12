import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class IdService {

  constructor() { }

  set id(id: string) {
    localStorage.setItem('id', id);
  }

  get id() {
    return localStorage.getItem('id') as string;
  }

  removeId() {
    localStorage.removeItem('id');
  }
}
