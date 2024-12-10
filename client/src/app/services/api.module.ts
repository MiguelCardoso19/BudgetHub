import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration, ApiConfigurationParams } from './api-configuration';

import { UserCredentialsControllerService } from './services/user-credentials-controller.service';
import { SupplierControllerService } from './services/supplier-controller.service';
import { MovementControllerService } from './services/movement-controller.service';
import { InvoiceControllerService } from './services/invoice-controller.service';
import { BudgetTypeControllerService } from './services/budget-type-controller.service';
import { BudgetSubtypeControllerService } from './services/budget-subtype-controller.service';
import { PaymentControllerService } from './services/payment-controller.service';
import { AuthenticationControllerService } from './services/authentication-controller.service';

/**
 * Module that provides all services and configuration.
 */
@NgModule({
  imports: [],
  exports: [],
  declarations: [],
  providers: [
    UserCredentialsControllerService,
    SupplierControllerService,
    MovementControllerService,
    InvoiceControllerService,
    BudgetTypeControllerService,
    BudgetSubtypeControllerService,
    PaymentControllerService,
    AuthenticationControllerService,
    ApiConfiguration
  ],
})
export class ApiModule {
  static forRoot(params: ApiConfigurationParams): ModuleWithProviders<ApiModule> {
    return {
      ngModule: ApiModule,
      providers: [
        {
          provide: ApiConfiguration,
          useValue: params
        }
      ]
    }
  }

  constructor(
    @Optional() @SkipSelf() parentModule: ApiModule,
    @Optional() http: HttpClient
  ) {
    if (parentModule) {
      throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
    }
    if (!http) {
      throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
      'See also https://github.com/angular/angular/issues/20575');
    }
  }
}
