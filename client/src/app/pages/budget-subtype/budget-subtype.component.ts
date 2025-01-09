import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BudgetSubtypeControllerService } from '../../services/services/budget-subtype-controller.service';
import { BudgetTypeControllerService } from '../../services/services/budget-type-controller.service';
import { BudgetSubtypeDto } from '../../services/models/budget-subtype-dto';
import { BudgetTypeDto } from '../../services/models/budget-type-dto';
import { ErrorHandlingService } from '../../services/error-handling/error-handling.service';
import { Pageable } from '../../services/models/pageable';
import { ConfirmPopUpComponent } from '../confirm-pop-up/confirm-pop-up.component';

@Component({
  selector: 'app-budget-subtype',
  templateUrl: './budget-subtype.component.html',
  standalone: true,
  imports: [FormsModule, CommonModule, ConfirmPopUpComponent],
  styleUrls: ['./budget-subtype.component.scss'],
})
export class BudgetSubtypeComponent implements OnInit {
  budgetSubtypes: BudgetSubtypeDto[] = [];
  budgetTypes: BudgetTypeDto[] = [];
  newBudgetSubtype: BudgetSubtypeDto = { id: '', name: '', description: '', budgetTypeId: '', correlationId: '', version: 0, availableFunds: 0 };
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalItems: number = 0;
  errorMsg: string[] = [];
  successMsg: string = '';
  showCreateForm: boolean = false;
  sortBy: string = 'name';
  sortDirection: string = 'asc';
  showDeleteModal: boolean = false;
  selectedBudgetSubtypeId: string = '';

  constructor(
    private budgetSubtypeService: BudgetSubtypeControllerService,
    private budgetTypeService: BudgetTypeControllerService,
    private errorHandlingService: ErrorHandlingService
  ) {}

  ngOnInit(): void {
    this.loadBudgetSubtypes();
    this.loadBudgetTypes();
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.itemsPerPage);
  }

  loadBudgetSubtypes(): void {
    const pageable: Pageable = {
      page: this.currentPage - 1,
      size: this.itemsPerPage,
      sort: [`${this.sortBy},${this.sortDirection}`],
    };

    this.budgetSubtypeService.findAllSubtypes({ pageable }).subscribe({
      next: (response) => this.handleResponse(response),
      error: (err) => this.handleError(err),
    });
  }

  loadBudgetTypes(): void {
    const pageable: Pageable = {
      page: this.currentPage - 1,
      size: this.itemsPerPage,
      sort: [`${this.sortBy},${this.sortDirection}`],
    };

    this.budgetTypeService.findAllBudgetTypes({ pageable }).subscribe({
      next: (response) => this.handleBudgetTypeResponse(response),
      error: (err) => this.handleError(err),
    });
  }

  create(): void {
    if (this.validateForm()) {
      this.budgetSubtypeService.addSubtype({ body: this.newBudgetSubtype }).subscribe({
        next: () => {
          this.setSuccessMessage('Budget Subtype created successfully!');
          this.closeCreateForm();
          this.loadBudgetSubtypes();
        },
        error: (err) => this.handleError(err),
      });
    }
  }

  getBudgetTypeName(id: string | null | undefined): string {
    const budgetType = this.budgetTypes.find((bt) => bt.id === id);
    return budgetType ? budgetType.name : 'Unknown';
  }

  setSuccessMessage(msg: string): void {
    this.successMsg = msg;
    setTimeout(() => (this.successMsg = ''), 4000);
  }

  changePageSize(): void {
    this.currentPage = 1;
    this.loadBudgetSubtypes();
  }

  applySort(): void {
    this.currentPage = 1;
    this.loadBudgetSubtypes();
  }

  openCreateForm(): void {
    this.showCreateForm = true;
    this.newBudgetSubtype = { id: '', name: '', description: '', budgetTypeId: '', correlationId: '', version: 0, availableFunds: 0 };
  }

  closeCreateForm(): void {
    this.showCreateForm = false;
    this.errorMsg = [];
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.loadBudgetSubtypes();
  }

  private handleResponse(response: any): void {
    if (response && response.content) {
      this.budgetSubtypes = response.content;
      this.totalItems = response.totalElements || 0;
    } else {
      this.budgetSubtypes = [];
      this.totalItems = 0;
    }
  }

  private validateForm(): boolean {
    this.errorMsg = [];
    if (!this.newBudgetSubtype.name.trim()) {
      this.errorMsg.push('Name is required.');
    }
    if (!this.newBudgetSubtype.description.trim()) {
      this.errorMsg.push('Description is required.');
    }
    if (!this.newBudgetSubtype.budgetTypeId) {
      this.errorMsg.push('Budget Type is required.');
    }
    return this.errorMsg.length === 0;
  }

  private handleBudgetTypeResponse(response: any): void {
    if (response instanceof Blob) {
      this.parseBlobResponse(response);
    } else {
      this.processResponseContent(response);
    }
  }

  private parseBlobResponse(blob: Blob): void {
    const reader = new FileReader();
    reader.onload = () => {
      try {
        const jsonResponse = JSON.parse(reader.result as string);
        this.processResponseContent(jsonResponse);
      } catch (error) {
        console.error('Error parsing JSON from Blob', error);
        this.resetBudgetTypeData();
      }
    };
    reader.readAsText(blob);
  }

  private processResponseContent(response: any): void {
    if (response && response.content) {
      this.budgetTypes = response.content.map((item: any) => ({
        id: item.id || '',
        version: item.version || 0,
        name: item.name || '',
        availableFunds: item.availableFunds || 0,
        description: item.description || '',
        correlationId: item.correlationId || ''
      }));
      this.totalItems = response.totalElements || 0;
    } else {
      console.error('Unexpected API response:', response);
      this.resetBudgetTypeData();
    }
  }

  private resetBudgetTypeData(): void {
    this.budgetTypes = [];
    this.totalItems = 0;
  }

  viewBudgetSubtypeInfo(subtype: BudgetSubtypeDto): void {
    console.log('Viewing Budget Subtype Info:', subtype);
  }

  delete(id: string): void {
    this.selectedBudgetSubtypeId = id;
    this.showDeleteModal = true;
  }

  confirmDelete(): void {
    const params = { id: this.selectedBudgetSubtypeId };
    this.budgetSubtypeService.deleteSubtype(params).subscribe({
      next: () => {
        this.setSuccessMessage('Budget Subtype deleted successfully!');
        this.showDeleteModal = false;
        this.loadBudgetSubtypes();
      },
      error: (err) => {
        this.handleError(err);
        this.showDeleteModal = false;
      },
    });
  }

  cancelDelete(): void {
    this.showDeleteModal = false;
  }

  private handleError(err: any): void {
    this.errorMsg = this.errorHandlingService.handleError(err);
  }
}
