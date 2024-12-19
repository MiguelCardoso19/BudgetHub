import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BudgetTypeControllerService } from '../../services/services/budget-type-controller.service';
import { BudgetTypeDto } from '../../services/models/budget-type-dto';
import { ErrorHandlingService } from '../../services/error-handling/error-handling.service';

@Component({
  selector: 'app-budget-type',
  templateUrl: './budget-type.component.html',
  standalone: true,
  imports: [FormsModule, CommonModule],
  styleUrls: ['./budget-type.component.scss']
})
export class BudgetTypeComponent {
  budgetTypes: string[] = ['Housing', 'Food', 'Transportation', 'Utilities', 'Healthcare', 'Education', 'Entertainment', 'Miscellaneous'];
  newBudgetType: BudgetTypeDto = { correlationId: '', id: '', version: 0, name: '', description: '', availableFunds: 0 };
  selectedBudgetType: string | null = null;

  currentPage: number = 1;
  itemsPerPage: number = 3;

  errorMsg: Array<string> = [];
  successMsg: string = '';
  showCreateForm: boolean = false;

  constructor(
    private budgetTypeService: BudgetTypeControllerService,
    private errorHandlingService: ErrorHandlingService
  ) {}

  openCreateForm(): void {
    this.showCreateForm = true;
    this.resetForm();
  }

  closeCreateForm(): void {
    this.showCreateForm = false;
    this.errorMsg = [];
  }

  create(): void {
    // @ts-ignore
    if (this.newBudgetType.name.trim() && this.newBudgetType.description.trim() && this.newBudgetType.availableFunds > 0) {
      this.budgetTypeService.createBudgetType({ body: this.newBudgetType }).subscribe({
        next: (response) => {
          if (response) {
            this.budgetTypes.push(response.name);
            this.setSuccessMessage('Budget Type created successfully!');
            this.closeCreateForm();
          }
        },
        error: (err) => {
          this.errorMsg = this.errorHandlingService.handleError(err);
        }
      });
    } else {
      this.errorMsg = ['Please fill in all fields correctly'];
    }
  }

  resetForm(): void {
    this.newBudgetType = { correlationId: '', id: '', version: 0, name: '', description: '', availableFunds: 0 };
  }

  get paginatedBudgetTypes(): string[] {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    return this.budgetTypes.slice(startIndex, startIndex + this.itemsPerPage);
  }

  changePage(page: number): void {
    this.currentPage = page;
  }

  addBudgetType(): void {
    if (this.newBudgetType.name.trim()) {
      this.budgetTypes.push(this.newBudgetType.name.trim());
      this.newBudgetType.name = '';
    }
  }

  updateBudgetType(updatedName: string | null): void {
    if (updatedName && this.selectedBudgetType) {
      const index = this.budgetTypes.indexOf(this.selectedBudgetType);
      if (index !== -1) {
        this.budgetTypes[index] = updatedName.trim();
        this.selectedBudgetType = null;
      }
    }
  }

  deleteBudgetType(budgetType: string): void {
    this.budgetTypes = this.budgetTypes.filter(bt => bt !== budgetType);
  }

  selectBudgetType(budgetType: string): void {
    this.selectedBudgetType = budgetType;
  }

  get totalPages(): number {
    return Math.ceil(this.budgetTypes.length / this.itemsPerPage);
  }

  setSuccessMessage(message: string): void {
    this.successMsg = message;

    setTimeout(() => {
      this.successMsg = '';
    }, 4000);
  }
}
