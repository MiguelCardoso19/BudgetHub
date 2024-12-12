import { Component, inject, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TokenService } from '../../services/token/token.service';
import { Subscription } from 'rxjs';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-layout',
  imports: [RouterModule, NgIf],
  templateUrl: './layout.component.html',
  standalone: true,
  styleUrl: './layout.component.scss'
})
export class LayoutComponent implements OnInit {
  tokenService = inject(TokenService);
  hasToken: boolean = false;
  tokenSubscription: Subscription | undefined;

  ngOnInit(): void {
    this.tokenSubscription = this.tokenService.token$.subscribe((token) => {
      this.hasToken = !!token;
    });
  }

  ngOnDestroy(): void {
    if (this.tokenSubscription) {
      this.tokenSubscription.unsubscribe();
    }
  }
}
