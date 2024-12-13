import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class StorageService {

    constructor() {
    }

    setItem(key: string, value: string): void {
        localStorage.setItem(key, value);
    }

    getItem(key: string): string | null {
        return localStorage.getItem(key);
    }

    removeItem(key: string): void {
        localStorage.removeItem(key);
    }

    set id(id: string) {
        this.setItem('id', id);
    }

    getId(): string | null {
        return this.getItem('id');
    }

    removeId(): void {
        this.removeItem('id');
    }

    set nif(nif: string) {
        this.setItem('nif', nif);
    }

    getNif(): string | null {
        return this.getItem('nif');
    }

    removeNif(): void {
        this.removeItem('nif');
    }

    set name(name: string) {
        this.setItem('name', name);
    }

    getName(): string | null {
        return this.getItem('name');
    }

    removeName(): void {
        this.removeItem('name');
    }
}
