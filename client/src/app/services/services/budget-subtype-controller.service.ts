import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { addSubtype } from '../fn/budget-subtype-controller/add-subtype';
import { AddSubtype$Params } from '../fn/budget-subtype-controller/add-subtype';
import { BudgetSubtypeDto } from '../models/budget-subtype-dto';
import { CustomPageDtoObject } from '../models/custom-page-dto-object';
import { deleteSubtype } from '../fn/budget-subtype-controller/delete-subtype';
import { DeleteSubtype$Params } from '../fn/budget-subtype-controller/delete-subtype';
import { findAllSubtypes } from '../fn/budget-subtype-controller/find-all-subtypes';
import { FindAllSubtypes$Params } from '../fn/budget-subtype-controller/find-all-subtypes';
import { findSubtypeById } from '../fn/budget-subtype-controller/find-subtype-by-id';
import { FindSubtypeById$Params } from '../fn/budget-subtype-controller/find-subtype-by-id';
import { updateSubtype } from '../fn/budget-subtype-controller/update-subtype';
import { UpdateSubtype$Params } from '../fn/budget-subtype-controller/update-subtype';


/**
 * Operations related to budget subtypes
 */
@Injectable({ providedIn: 'root' })
export class BudgetSubtypeControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `updateSubtype()` */
  static readonly UpdateSubtypePath = '/api/v1/budget/subtype/update';

  /**
   * Update an existing budget subtype.
   *
   * Updates the details of an existing budget subtype.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateSubtype()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateSubtype$Response(params: UpdateSubtype$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetSubtypeDto | null>> {
    return updateSubtype(this.http, this.rootUrl, params, context);
  }

  /**
   * Update an existing budget subtype.
   *
   * Updates the details of an existing budget subtype.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateSubtype$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateSubtype(params: UpdateSubtype$Params, context?: HttpContext): Observable<BudgetSubtypeDto | null> {
    return this.updateSubtype$Response(params, context).pipe(
      map((r: StrictHttpResponse<BudgetSubtypeDto | null>): BudgetSubtypeDto | null => r.body)
    );
  }

  /** Path part for operation `addSubtype()` */
  static readonly AddSubtypePath = '/api/v1/budget/subtype/create';

  /**
   * Create a new budget subtype.
   *
   * Creates a new budget subtype for a given budget type.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addSubtype()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addSubtype$Response(params: AddSubtype$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetSubtypeDto | null>> {
    return addSubtype(this.http, this.rootUrl, params, context);
  }

  /**
   * Create a new budget subtype.
   *
   * Creates a new budget subtype for a given budget type.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addSubtype$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addSubtype(params: AddSubtype$Params, context?: HttpContext): Observable<BudgetSubtypeDto | null> {
    return this.addSubtype$Response(params, context).pipe(
      map((r: StrictHttpResponse<BudgetSubtypeDto | null>): BudgetSubtypeDto | null => r.body)
    );
  }

  /** Path part for operation `findSubtypeById()` */
  static readonly FindSubtypeByIdPath = '/api/v1/budget/subtype/{id}';

  /**
   * Get a budget subtype by ID.
   *
   * Fetches a budget subtype by its ID.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findSubtypeById()` instead.
   *
   * This method doesn't expect any request body.
   */
  findSubtypeById$Response(params: FindSubtypeById$Params, context?: HttpContext): Observable<StrictHttpResponse<BudgetSubtypeDto | null>> {
    return findSubtypeById(this.http, this.rootUrl, params, context);
  }

  /**
   * Get a budget subtype by ID.
   *
   * Fetches a budget subtype by its ID.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findSubtypeById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findSubtypeById(params: FindSubtypeById$Params, context?: HttpContext): Observable<BudgetSubtypeDto | null> {
    return this.findSubtypeById$Response(params, context).pipe(
      map((r: StrictHttpResponse<BudgetSubtypeDto | null>): BudgetSubtypeDto | null => r.body)
    );
  }

  /** Path part for operation `findAllSubtypes()` */
  static readonly FindAllSubtypesPath = '/api/v1/budget/subtype/all';

  /**
   * Get all budget subtypes.
   *
   * Fetches all budget subtypes, paginated.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `findAllSubtypes()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllSubtypes$Response(params: FindAllSubtypes$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
    return findAllSubtypes(this.http, this.rootUrl, params, context);
  }

  /**
   * Get all budget subtypes.
   *
   * Fetches all budget subtypes, paginated.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `findAllSubtypes$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  findAllSubtypes(params: FindAllSubtypes$Params, context?: HttpContext): Observable<CustomPageDtoObject> {
    return this.findAllSubtypes$Response(params, context).pipe(
      map((r: StrictHttpResponse<CustomPageDtoObject>): CustomPageDtoObject => r.body)
    );
  }

  /** Path part for operation `deleteSubtype()` */
  static readonly DeleteSubtypePath = '/api/v1/budget/subtype/delete/{id}';

  /**
   * Delete a budget subtype.
   *
   * Deletes a budget subtype by its ID.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteSubtype()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteSubtype$Response(params: DeleteSubtype$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteSubtype(this.http, this.rootUrl, params, context);
  }

  /**
   * Delete a budget subtype.
   *
   * Deletes a budget subtype by its ID.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteSubtype$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteSubtype(params: DeleteSubtype$Params, context?: HttpContext): Observable<void> {
    return this.deleteSubtype$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
