import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BudgetTypeControllerService } from '../../services/services/budget-type-controller.service';
import { BudgetTypeDto } from '../../services/models/budget-type-dto';
import { ErrorHandlingService } from '../../services/error-handling/error-handling.service';
import { Pageable } from '../../services/models/pageable';
import {ConfirmPopUpComponent} from '../confirm-pop-up/confirm-pop-up.component';
import {BudgetSubtypeDto} from '../../services/models/budget-subtype-dto';

@Component({
  selector: 'app-budget-type',
  templateUrl: './budget-type.component.html',
  standalone: true,
  imports: [FormsModule, CommonModule, ConfirmPopUpComponent],
  styleUrls: ['./budget-type.component.scss']
})
export class BudgetTypeComponent implements OnInit {
  budgetTypes: BudgetTypeDto[] = [];
  newBudgetType: BudgetTypeDto = { correlationId: '', id: '', version: 0, name: '', description: '', availableFunds: 0 };
  selectedBudgetType: BudgetTypeDto | null = null;
  editableBudgetType: BudgetTypeDto | null = null;
  currentPage: number = 1;
  itemsPerPage: number = 5;
  totalItems: number = 0;
  errorMsg: string[] = [];
  successMsg: string = '';
  showCreateForm: boolean = false;
  isEditable = false;
  sortBy: string = 'name';
  sortDirection: string = 'asc';
  showDeleteModal: boolean = false;
  showInfoForm: boolean = false;
  showEditForm: boolean = false;

  constructor(
    private budgetTypeService: BudgetTypeControllerService,
    private errorHandlingService: ErrorHandlingService
  ) {}

  ngOnInit(): void {
    this.loadBudgetTypes();
    this.editableBudgetType = { name: '', description: '', availableFunds: 0, correlationId: '0', id: '0', version:0};
  }

  loadBudgetTypes(): void {
    // @ts-ignore
    const pageable: Pageable = {page: this.currentPage - 1, size: this.itemsPerPage, sort: `${this.sortBy},${this.sortDirection}`};
    this.budgetTypeService.findAllBudgetTypes({ pageable }).subscribe({
      next: (response) => this.handleBudgetTypeResponse(response),
      error: (err) => this.errorHandlingService.handleError(err),
    });
  }

  create(): void {
    // @ts-ignore
    if (this.newBudgetType.name.trim() && this.newBudgetType.description.trim() && this.newBudgetType.availableFunds > 0) {
      this.budgetTypeService.createBudgetType({ body: this.newBudgetType }).subscribe({
        next: () => {
          this.setSuccessMessage('Budget Type created successfully!');
          this.closeCreateForm();
          this.loadBudgetTypes();
        },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleError(err);
        }
      });
    } else {
      this.errorMsg = ['Please fill in all fields correctly'];
    }
  }

  delete(id: string): void {
    this.showDeleteModal = true;
    this.selectedBudgetType = this.budgetTypes.find(b => b.id === id) || null;
  }

  update(): void {
    this.budgetTypeService.updateBudgetType({ body: this.newBudgetType }).subscribe({
      next: () => {
        this.setSuccessMessage('Budget type updated successfully.');
        this.isEditable = false;
        this.closeInfoForm();
        this.closeEditForm();
        this.loadBudgetTypes();
      },
      error: (err) => {
        this.errorMsg = this.errorHandlingService.handleError(err);
      }
    });
  }

  confirmDelete(): void {
    if (this.selectedBudgetType) {
      this.budgetTypeService.deleteBudgetType({ id: this.selectedBudgetType.id }).subscribe({
        next: () => {
          this.setSuccessMessage('Budget Type deleted successfully!');
          this.loadBudgetTypes();
          this.showDeleteModal = false;
          this.closeInfoForm();
        },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleError(err);
          this.showDeleteModal = false;
        }
      });
    }
  }

  closeInfoForm() {
    this.showInfoForm = false;
    this.showEditForm = false;
    this.selectedBudgetType = null;
  }

  cancelDelete() {
    this.showDeleteModal = false;
    this.isEditable = false;
  }

  resetForm(): void {
    this.newBudgetType = { correlationId: '', id: '', version: 0, name: '', description: '', availableFunds: 0 };
  }

  setSuccessMessage(msg: string): void {
    this.successMsg = msg;
    setTimeout(() => this.successMsg = '', 4000);
  }

  get totalPages(): number {
    return Math.ceil(this.totalItems / this.itemsPerPage);
  }

  changePageSize(): void {
    this.currentPage = 1;
    this.loadBudgetTypes();
  }

  applySort(): void {
    this.currentPage = 1;
    this.loadBudgetTypes();
  }

  openCreateForm(): void {
    this.showCreateForm = true;
    this.resetForm();
  }

  closeCreateForm(): void {
    this.showCreateForm = false;
    this.errorMsg = [];
  }

  setEditableAsFalse() {
    this.isEditable = false;
  }

  toggleEdit(): void {
    this.isEditable = !this.isEditable;
  }

  closeEditForm(): void {
    this.showEditForm = false;
    this.errorMsg = [];
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.loadBudgetTypes();
  }

  viewBudgetTypeInfo(budgetType: BudgetSubtypeDto) {
    this.newBudgetType = {... budgetType};
    this.showEditForm = true;
    this.isEditable = false;
    this.showCreateForm = false;
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
}
