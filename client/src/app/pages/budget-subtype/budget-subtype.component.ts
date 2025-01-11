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
  isEditable: boolean = false;
  showEditForm: boolean = false;

  constructor(
    private budgetSubtypeService: BudgetSubtypeControllerService,
    private budgetTypeService: BudgetTypeControllerService,
    private errorHandlingService: ErrorHandlingService
  ) {}

  ngOnInit(): void {
    this.loadBudgetSubtypes();
    this.loadBudgetTypes();
  }

  loadBudgetSubtypes(): void {
    const pageable: Pageable = {
      page: this.currentPage - 1,
      size: this.itemsPerPage,
      sort: [`${this.sortBy},${this.sortDirection}`],
    };

    this.budgetSubtypeService.findAllSubtypes({ pageable }).subscribe({
      next: (response) => this.handleBudgetSubtypeResponse(response),
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

  delete(id: string): void {
    this.selectedBudgetSubtypeId = id;
    this.showDeleteModal = true;
  }

  closeEditForm(): void {
    this.showEditForm = false;
    this.errorMsg = [];
  }

  update(): void {
    if (this.validateForm()) {
      const updatePayload = {
        id: this.newBudgetSubtype.id,
        budgetTypeId: this.newBudgetSubtype.budgetTypeId,
        name: this.newBudgetSubtype.name,
        description: this.newBudgetSubtype.description,
        version: this.newBudgetSubtype.version,
        availableFunds: this.newBudgetSubtype.availableFunds,
        correlationId: '',
      };

      this.budgetSubtypeService.updateSubtype({ body: updatePayload }).subscribe({
        next: () => {
          this.setSuccessMessage('Budget Subtype updated successfully!');
          this.closeEditForm();
          this.loadBudgetSubtypes();
        },
        error: (err) => this.handleError(err),
      });
    }
  }

  confirmDelete(): void {
    const params = { id: this.selectedBudgetSubtypeId };
    this.budgetSubtypeService.deleteSubtype(params).subscribe({
      next: () => {
        this.setSuccessMessage('Budget Subtype deleted successfully!');
        this.showDeleteModal = false;
        this.closeEditForm();
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

  setEditableAsFalse() {
    this.isEditable = false;
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

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.itemsPerPage);
  }

  toggleEdit(): void {
    this.isEditable = !this.isEditable;
  }

  viewBudgetSubtypeInfo(subtype: BudgetSubtypeDto): void {
    this.newBudgetSubtype = { ...subtype };
    this.showEditForm = true;
    this.isEditable = false;
    this.showCreateForm = false;
  }

  private handleBudgetSubtypeResponse(response: any): void {
    this.parseResponse(response, this.processBudgetSubtypes.bind(this), this.resetBudgetSubtypeData.bind(this));
  }

  private handleBudgetTypeResponse(response: any): void {
    this.parseResponse(response, this.processResponseContent.bind(this), this.resetBudgetTypeData.bind(this));
  }

  private parseResponse(response: any, onSuccess: (parsedResponse: any) => void, onError: () => void): void {
    if (response instanceof Blob) {
      const reader = new FileReader();
      reader.onload = () => {
        try {
          const jsonResponse = JSON.parse(reader.result as string);
          onSuccess(jsonResponse);
        } catch (error) {
          console.error('Error parsing JSON from Blob', error);
          onError();
        }
      };
      reader.readAsText(response);
    } else {
      try {
        onSuccess(response);
      } catch (error) {
        console.error('Error processing response', error);
        onError();
      }
    }
  }

  private processBudgetSubtypes(response: any): void {
    if (response && response.content) {
      this.budgetSubtypes = response.content.map((item: any) => ({
        id: item.id || '',
        name: item.name || '',
        description: item.description || '',
        availableFunds: item.availableFunds || 0,
        budgetTypeId: item.budgetType?.id || '',
        budgetTypeName: item.budgetType?.name || 'Unknown',
        version: item.version
      }));
      this.totalItems = response.totalElements || 0;
    } else {
      this.resetBudgetSubtypeData();
    }
  }

  private processResponseContent(response: any): void {
    if (response && response.content) {
      this.budgetTypes = response.content.map((item: any) => ({
        id: item.id || '',
        version: item.version || 0,
        name: item.name || '',
        availableFunds: item.availableFunds || 0,
        description: item.description || '',
        correlationId: item.correlationId || '',
      }));
      this.totalItems = response.totalElements || 0;
    } else {
      console.error('Unexpected API response:', response);
      this.resetBudgetTypeData();
    }
  }

  private resetBudgetSubtypeData(): void {
    this.budgetSubtypes = [];
    this.totalItems = 0;
  }

  private resetBudgetTypeData(): void {
    this.budgetTypes = [];
    this.totalItems = 0;
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

  private handleError(err: any): void {
    this.errorMsg = this.errorHandlingService.handleError(err);
  }
}
