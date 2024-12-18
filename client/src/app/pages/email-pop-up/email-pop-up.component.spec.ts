import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailPopUpComponent } from './email-pop-up.component';

describe('EmailPopUpComponent', () => {
  let component: EmailPopUpComponent;
  let fixture: ComponentFixture<EmailPopUpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmailPopUpComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmailPopUpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
