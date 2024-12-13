import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class StorageService {

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

  set nif(nif: string) {
    localStorage.setItem('nif', nif);
  }

  get nif() {
    return localStorage.getItem('nif') as string;
  }

  removeNif() {
    localStorage.removeItem('nif');
  }

  set name(name: string) {
    localStorage.setItem('name', name);
  }

  get name() {
    return localStorage.getItem('name') as string;
  }

  removeName() {
    localStorage.removeItem('name');
  }
}
