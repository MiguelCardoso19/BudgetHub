import {Component} from '@angular/core';
import {StorageService} from '../../services/storage/storage.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  standalone: true,
  imports: [],
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {
  errorMsg: Array<string> = [];
  firstName: string = '';

  constructor(
    private storageService: StorageService,
    private router: Router,
  ) {
    this.firstName =  this.getNameFromStorage();
  }

  formatText(value: string): string {
    if (!value) return '';
    return value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
  }

  private getNameFromStorage(): string {
    return this.storageService.getName() as string;
  }
}
