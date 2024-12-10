import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { create } from '../fn/supplier-controller/create';
import { Create$Params } from '../fn/supplier-controller/create';
import { CustomPageDtoObject } from '../models/custom-page-dto-object';
import { delete1 } from '../fn/supplier-controller/delete-1';
import { Delete1$Params } from '../fn/supplier-controller/delete-1';
import { getAll } from '../fn/supplier-controller/get-all';
import { GetAll$Params } from '../fn/supplier-controller/get-all';
import { getById } from '../fn/supplier-controller/get-by-id';
import { GetById$Params } from '../fn/supplier-controller/get-by-id';
import { SupplierDto } from '../models/supplier-dto';
import { update1 } from '../fn/supplier-controller/update-1';
import { Update1$Params } from '../fn/supplier-controller/update-1';


/**
 * Operations related to suppliers management
 */
@Injectable({ providedIn: 'root' })
export class SupplierControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `update1()` */
  static readonly Update1Path = '/api/v1/supplier/update';

  /**
   * Update an existing supplier.
   *
   * Updates details of an existing supplier in the system.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `update1()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update1$Response(params: Update1$Params, context?: HttpContext): Observable<StrictHttpResponse<SupplierDto>> {
    return update1(this.http, this.rootUrl, params, context);
  }

  /**
   * Update an existing supplier.
   *
   * Updates details of an existing supplier in the system.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `update1$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  update1(params: Update1$Params, context?: HttpContext): Observable<SupplierDto> {
    return this.update1$Response(params, context).pipe(
      map((r: StrictHttpResponse<SupplierDto>): SupplierDto => r.body)
    );
  }

  /** Path part for operation `create()` */
  static readonly CreatePath = '/api/v1/supplier/create';

  /**
   * Create a new supplier.
   *
   * Creates a new supplier in the system with validated supplier details.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `create()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create$Response(params: Create$Params, context?: HttpContext): Observable<StrictHttpResponse<SupplierDto>> {
    return create(this.http, this.rootUrl, params, context);
  }

  /**
   * Create a new supplier.
   *
   * Creates a new supplier in the system with validated supplier details.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `create$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  create(params: Create$Params, context?: HttpContext): Observable<SupplierDto> {
    return this.create$Response(params, context).pipe(
      map((r: StrictHttpResponse<SupplierDto>): SupplierDto => r.body)
    );
  }

  /** Path part for operation `getById()` */
  static readonly GetByIdPath = '/api/v1/supplier/{id}';

  /**
   * Get supplier by ID.
   *
   * Fetches a specific supplier by its ID.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getById()` instead.
   *
   * This method doesn't expect any request body.
   */
  getById$Response(params: GetById$Params, context?: HttpContext): Observable<StrictHttpResponse<SupplierDto>> {
    return getById(this.http, this.rootUrl, params, context);
  }

  /**
   * Get supplier by ID.
   *
   * Fetches a specific supplier by its ID.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getById(params: GetById$Params, context?: HttpContext): Observable<SupplierDto> {
    return this.getById$Response(params, context).pipe(
      map((r: StrictHttpResponse<SupplierDto>): SupplierDto => r.body)
    );
  }

  /** Path part for operation `delete1()` */
  static readonly Delete1Path = '/api/v1/supplier/{id}';

  /**
   * Delete a supplier.
   *
   * Deletes an existing supplier by its ID.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `delete1()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete1$Response(params: Delete1$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return delete1(this.http, this.rootUrl, params, context);
  }

  /**
   * Delete a supplier.
   *
   * Deletes an existing supplier by its ID.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `delete1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  delete1(params: Delete1$Params, context?: HttpContext): Observable<void> {
    return this.delete1$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `getAll()` */
  static readonly GetAllPath = '/api/v1/supplier/all';

  /**
   * Get all suppliers.
   *
   * Fetches a paginated list of all suppliers.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAll()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll$Response(params: GetAll$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
    return getAll(this.http, this.rootUrl, params, context);
  }

  /**
   * Get all suppliers.
   *
   * Fetches a paginated list of all suppliers.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAll$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll(params: GetAll$Params, context?: HttpContext): Observable<CustomPageDtoObject> {
    return this.getAll$Response(params, context).pipe(
      map((r: StrictHttpResponse<CustomPageDtoObject>): CustomPageDtoObject => r.body)
    );
  }

}
