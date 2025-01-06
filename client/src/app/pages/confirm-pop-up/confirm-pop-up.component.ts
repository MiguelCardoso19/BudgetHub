import {Component, EventEmitter, Input, Output} from '@angular/core';
import {NgClass, NgIf} from '@angular/common';

@Component({
  selector: 'app-confirm-pop-up',
  imports: [NgIf, NgClass],
  templateUrl: './confirm-pop-up.component.html',
  standalone: true,
  styleUrl: './confirm-pop-up.component.scss'
})
export class ConfirmPopUpComponent {
  @Input() showModal: boolean = false;
  @Input() message: string = 'Are you sure?';
  @Output() confirm = new EventEmitter<void>();
  @Output() cancel = new EventEmitter<void>();

  onConfirm() {
    this.confirm.emit();
  }

  onCancel() {
    this.cancel.emit();
  }
}
