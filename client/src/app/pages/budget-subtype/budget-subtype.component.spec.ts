import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BudgetSubtypeComponent } from './budget-subtype.component';

describe('BudgetSubtypeComponent', () => {
  let component: BudgetSubtypeComponent;
  let fixture: ComponentFixture<BudgetSubtypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BudgetSubtypeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BudgetSubtypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
