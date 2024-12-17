import {Component} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {StorageService} from '../../services/storage/storage.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  standalone: true,
  imports: [NgForOf, NgIf],
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  errorMsg: Array<string> = [];
  firstName: string = '';

  constructor(
    private storageService: StorageService,
  ) {
    this.firstName =  this.getNameFromStorage();
  }

  formatText(value: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  private getNameFromStorage(): string {
    return this.storageService.getName() as string;
  }
}
