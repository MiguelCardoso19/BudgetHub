import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { create1 } from '../fn/movement-controller/create-1';
import { Create1$Params } from '../fn/movement-controller/create-1';
import { CustomPageDtoObject } from '../models/custom-page-dto-object';
import { delete2 } from '../fn/movement-controller/delete-2';
import { Delete2$Params } from '../fn/movement-controller/delete-2';
import { exportMovementsReport } from '../fn/movement-controller/export-movements-report';
import { ExportMovementsReport$Params } from '../fn/movement-controller/export-movements-report';
import { getAll1 } from '../fn/movement-controller/get-all-1';
import { GetAll1$Params } from '../fn/movement-controller/get-all-1';
import { getByBudgetSubtype } from '../fn/movement-controller/get-by-budget-subtype';
import { GetByBudgetSubtype$Params } from '../fn/movement-controller/get-by-budget-subtype';
import { getByBudgetType } from '../fn/movement-controller/get-by-budget-type';
import { GetByBudgetType$Params } from '../fn/movement-controller/get-by-budget-type';
import { getById1 } from '../fn/movement-controller/get-by-id-1';
import { GetById1$Params } from '../fn/movement-controller/get-by-id-1';
import { MovementDto } from '../models/movement-dto';
import { update2 } from '../fn/movement-controller/update-2';
import { Update2$Params } from '../fn/movement-controller/update-2';


/**
 * Operations related to financial movements
 */
@Injectable({ providedIn: 'root' })
export class MovementControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `update2()` */
  static readonly Update2Path = '/api/v1/movement/update';

  /**
   * Update an existing movement.
   *
   * Updates an existing movement by providing updated details.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update2()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update2$Response(params: Update2$Params, context?: HttpContext): Observable<StrictHttpResponse<MovementDto>> {
    return update2(this.http, this.rootUrl, params, context);
  }

  /**
   * Update an existing movement.
   *
   * Updates an existing movement by providing updated details.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `update2$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update2(params: Update2$Params, context?: HttpContext): Observable<MovementDto> {
    return this.update2$Response(params, context).pipe(
      map((r: StrictHttpResponse<MovementDto>): MovementDto => r.body)
    );
  }

  /** Path part for operation `exportMovementsReport()` */
  static readonly ExportMovementsReportPath = '/api/v1/movement/export-movements-report';

  /**
   * Export movements report.
   *
   * Exports a report of movements within a date range and status, sent to the user's email.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `exportMovementsReport()` instead.
   *
   * This method doesn't expect any request body.
   */
  exportMovementsReport$Response(params?: ExportMovementsReport$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return exportMovementsReport(this.http, this.rootUrl, params, context);
  }

  /**
   * Export movements report.
   *
   * Exports a report of movements within a date range and status, sent to the user's email.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `exportMovementsReport$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  exportMovementsReport(params?: ExportMovementsReport$Params, context?: HttpContext): Observable<void> {
    return this.exportMovementsReport$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `create1()` */
  static readonly Create1Path = '/api/v1/movement/create';

  /**
   * Create a new movement.
   *
   * Creates a new financial movement.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create1()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create1$Response(params: Create1$Params, context?: HttpContext): Observable<StrictHttpResponse<MovementDto>> {
    return create1(this.http, this.rootUrl, params, context);
  }

  /**
   * Create a new movement.
   *
   * Creates a new financial movement.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `create1$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create1(params: Create1$Params, context?: HttpContext): Observable<MovementDto> {
    return this.create1$Response(params, context).pipe(
      map((r: StrictHttpResponse<MovementDto>): MovementDto => r.body)
    );
  }

  /** Path part for operation `getById1()` */
  static readonly GetById1Path = '/api/v1/movement/{id}';

  /**
   * Get a movement by ID.
   *
   * Fetches a movement by its unique ID.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getById1()` instead.
   *
   * This method doesn't expect any request body.
   */
  getById1$Response(params: GetById1$Params, context?: HttpContext): Observable<StrictHttpResponse<MovementDto>> {
    return getById1(this.http, this.rootUrl, params, context);
  }

  /**
   * Get a movement by ID.
   *
   * Fetches a movement by its unique ID.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getById1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getById1(params: GetById1$Params, context?: HttpContext): Observable<MovementDto> {
    return this.getById1$Response(params, context).pipe(
      map((r: StrictHttpResponse<MovementDto>): MovementDto => r.body)
    );
  }

  /** Path part for operation `delete2()` */
  static readonly Delete2Path = '/api/v1/movement/{id}';

  /**
   * Delete a movement by ID.
   *
   * Deletes an existing movement based on the given ID.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete2()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete2$Response(params: Delete2$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return delete2(this.http, this.rootUrl, params, context);
  }

  /**
   * Delete a movement by ID.
   *
   * Deletes an existing movement based on the given ID.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete2(params: Delete2$Params, context?: HttpContext): Observable<void> {
    return this.delete2$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `getByBudgetType()` */
  static readonly GetByBudgetTypePath = '/api/v1/movement/budget-type/{budgetTypeId}';

  /**
   * Fetch movements by budget type.
   *
   * Retrieves movements filtered by budget type ID.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getByBudgetType()` instead.
   *
   * This method doesn't expect any request body.
   */
  getByBudgetType$Response(params: GetByBudgetType$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
    return getByBudgetType(this.http, this.rootUrl, params, context);
  }

  /**
   * Fetch movements by budget type.
   *
   * Retrieves movements filtered by budget type ID.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getByBudgetType$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getByBudgetType(params: GetByBudgetType$Params, context?: HttpContext): Observable<CustomPageDtoObject> {
    return this.getByBudgetType$Response(params, context).pipe(
      map((r: StrictHttpResponse<CustomPageDtoObject>): CustomPageDtoObject => r.body)
    );
  }

  /** Path part for operation `getByBudgetSubtype()` */
  static readonly GetByBudgetSubtypePath = '/api/v1/movement/budget-subtype/{budgetSubtypeId}';

  /**
   * Fetch movements by budget subtype.
   *
   * Retrieves movements filtered by budget subtype ID.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getByBudgetSubtype()` instead.
   *
   * This method doesn't expect any request body.
   */
  getByBudgetSubtype$Response(params: GetByBudgetSubtype$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
    return getByBudgetSubtype(this.http, this.rootUrl, params, context);
  }

  /**
   * Fetch movements by budget subtype.
   *
   * Retrieves movements filtered by budget subtype ID.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getByBudgetSubtype$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getByBudgetSubtype(params: GetByBudgetSubtype$Params, context?: HttpContext): Observable<CustomPageDtoObject> {
    return this.getByBudgetSubtype$Response(params, context).pipe(
      map((r: StrictHttpResponse<CustomPageDtoObject>): CustomPageDtoObject => r.body)
    );
  }

  /** Path part for operation `getAll1()` */
  static readonly GetAll1Path = '/api/v1/movement/all';

  /**
   * Fetch all movements with pagination.
   *
   * Returns a paginated list of all movements.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAll1()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll1$Response(params: GetAll1$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
    return getAll1(this.http, this.rootUrl, params, context);
  }

  /**
   * Fetch all movements with pagination.
   *
   * Returns a paginated list of all movements.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAll1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll1(params: GetAll1$Params, context?: HttpContext): Observable<CustomPageDtoObject> {
    return this.getAll1$Response(params, context).pipe(
      map((r: StrictHttpResponse<CustomPageDtoObject>): CustomPageDtoObject => r.body)
    );
  }

}
