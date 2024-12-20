import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { BudgetTypeDto } from '../models/budget-type-dto';
import { createBudgetType } from '../fn/budget-type-controller/create-budget-type';
import { CreateBudgetType$Params } from '../fn/budget-type-controller/create-budget-type';
import { CustomPageDtoObject } from '../models/custom-page-dto-object';
import { deleteBudgetType } from '../fn/budget-type-controller/delete-budget-type';
import { DeleteBudgetType$Params } from '../fn/budget-type-controller/delete-budget-type';
import { findAllBudgetTypes } from '../fn/budget-type-controller/find-all-budget-types';
import { FindAllBudgetTypes$Params } from '../fn/budget-type-controller/find-all-budget-types';
import { findBudgetTypeById } from '../fn/budget-type-controller/find-budget-type-by-id';
import { FindBudgetTypeById$Params } from '../fn/budget-type-controller/find-budget-type-by-id';
import { updateBudgetType } from '../fn/budget-type-controller/update-budget-type';
import { UpdateBudgetType$Params } from '../fn/budget-type-controller/update-budget-type';
import {TokenService} from '../token/token.service';
import {StorageService} from '../storage/storage.service';


/**
 * Operations related to Budget Types
 */
@Injectable({ providedIn: 'root' })
export class BudgetTypeControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient, private tokenService: TokenService, private storageService: StorageService) {
    super(config, http);
  }

  /** Path part for operation `updateBudgetType()` */
  static readonly UpdateBudgetTypePath = '/api/v1/budget/type/update';

  /**
   * Update an existing Budget Type.
   *
   * Updates an existing Budget Type. If the Budget Type does not exist or a conflict occurs, the operation will throw appropriate errors.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateBudgetType()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateBudgetType$Response(params: UpdateBudgetType$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetTypeDto | null>> {
    return updateBudgetType(this.tokenService, this.storageService, this.http, this.rootUrl, params, context);
  }

  /**
   * Update an existing Budget Type.
   *
   * Updates an existing Budget Type. If the Budget Type does not exist or a conflict occurs, the operation will throw appropriate errors.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateBudgetType$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateBudgetType(params: UpdateBudgetType$Params, context?: HttpContext): Observable<BudgetTypeDto | null> {
    return this.updateBudgetType$Response(params, context).pipe(
      map((r: StrictHttpResponse<BudgetTypeDto | null>): BudgetTypeDto | null => r.body)
    );
  }

  /** Path part for operation `createBudgetType()` */
  static readonly CreateBudgetTypePath = '/api/v1/budget/type/create';

  /**
   * Create a new Budget Type.
   *
   * Creates a new Budget Type. This operation will check for existing budget types and throw an error if a duplicate is found.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createBudgetType()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createBudgetType$Response(params: CreateBudgetType$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetTypeDto | null>> {
    return createBudgetType(this.http, this.rootUrl, params, context);
  }

  /**
   * Create a new Budget Type.
   *
   * Creates a new Budget Type. This operation will check for existing budget types and throw an error if a duplicate is found.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createBudgetType$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createBudgetType(params: CreateBudgetType$Params, context?: HttpContext): Observable<BudgetTypeDto | null> {
    return this.createBudgetType$Response(params, context).pipe(
      map((r: StrictHttpResponse<BudgetTypeDto | null>): BudgetTypeDto | null => r.body)
    );
  }

  /** Path part for operation `findBudgetTypeById()` */
  static readonly FindBudgetTypeByIdPath = '/api/v1/budget/type/{id}';

  /**
   * Get a Budget Type by ID.
   *
   * Fetches a specific Budget Type by its unique ID. Returns a 404 if the Budget Type is not found.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findBudgetTypeById()` instead.
   *
   * This method doesn't expect any request body.
   */
  findBudgetTypeById$Response(params: FindBudgetTypeById$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetTypeDto | null>> {
    return findBudgetTypeById(this.http, this.rootUrl, params, context);
  }

  /**
   * Get a Budget Type by ID.
   *
   * Fetches a specific Budget Type by its unique ID. Returns a 404 if the Budget Type is not found.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findBudgetTypeById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findBudgetTypeById(params: FindBudgetTypeById$Params, context?: HttpContext): Observable<BudgetTypeDto | null> {
    return this.findBudgetTypeById$Response(params, context).pipe(
      map((r: StrictHttpResponse<BudgetTypeDto | null>): BudgetTypeDto | null => r.body)
    );
  }

  /** Path part for operation `findAllBudgetTypes()` */
  static readonly FindAllBudgetTypesPath = '/api/v1/budget/type/all';

  /**
   * Get all Budget Types.
   *
   * Retrieves all Budget Types, supporting pagination. Returns a paginated list of Budget Types.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllBudgetTypes()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllBudgetTypes$Response(params: FindAllBudgetTypes$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
    return findAllBudgetTypes(this.http, this.rootUrl, params, context);
  }

  /**
   * Get all Budget Types.
   *
   * Retrieves all Budget Types, supporting pagination. Returns a paginated list of Budget Types.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllBudgetTypes$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllBudgetTypes(params: FindAllBudgetTypes$Params, context?: HttpContext): Observable<CustomPageDtoObject> {
    return this.findAllBudgetTypes$Response(params, context).pipe(
      map((r: StrictHttpResponse<CustomPageDtoObject>): CustomPageDtoObject => r.body)
    );
  }

  /** Path part for operation `deleteBudgetType()` */
  static readonly DeleteBudgetTypePath = '/api/v1/budget/type/delete/{id}';

  /**
   * Delete a Budget Type.
   *
   * Deletes a Budget Type by its unique ID. Throws a 404 error if the Budget Type is not found.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteBudgetType()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteBudgetType$Response(params: DeleteBudgetType$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteBudgetType(this.http, this.rootUrl, params, context);
  }

  /**
   * Delete a Budget Type.
   *
   * Deletes a Budget Type by its unique ID. Throws a 404 error if the Budget Type is not found.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteBudgetType$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteBudgetType(params: DeleteBudgetType$Params, context?: HttpContext): Observable<void> {
    return this.deleteBudgetType$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
