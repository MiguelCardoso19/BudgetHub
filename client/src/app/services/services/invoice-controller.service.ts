import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { attachBase64FileToInvoice } from '../fn/invoice-controller/attach-base-64-file-to-invoice';
import { AttachBase64FileToInvoice$Params } from '../fn/invoice-controller/attach-base-64-file-to-invoice';
import { attachMultipartFileToInvoice } from '../fn/invoice-controller/attach-multipart-file-to-invoice';
import { AttachMultipartFileToInvoice$Params } from '../fn/invoice-controller/attach-multipart-file-to-invoice';
import { createInvoice } from '../fn/invoice-controller/create-invoice';
import { CreateInvoice$Params } from '../fn/invoice-controller/create-invoice';
import { CustomPageDtoObject } from '../models/custom-page-dto-object';
import { deleteInvoice } from '../fn/invoice-controller/delete-invoice';
import { DeleteInvoice$Params } from '../fn/invoice-controller/delete-invoice';
import { getAllInvoices } from '../fn/invoice-controller/get-all-invoices';
import { GetAllInvoices$Params } from '../fn/invoice-controller/get-all-invoices';
import { getInvoiceById } from '../fn/invoice-controller/get-invoice-by-id';
import { GetInvoiceById$Params } from '../fn/invoice-controller/get-invoice-by-id';
import { InvoiceDto } from '../models/invoice-dto';
import { updateInvoice } from '../fn/invoice-controller/update-invoice';
import { UpdateInvoice$Params } from '../fn/invoice-controller/update-invoice';


/**
 * Operations related to Invoices
 */
@Injectable({ providedIn: 'root' })
export class InvoiceControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `updateInvoice()` */
  static readonly UpdateInvoicePath = '/api/v1/invoice/update';

  /**
   * Update an invoice.
   *
   * Updates an existing invoice record.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateInvoice()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateInvoice$Response(params: UpdateInvoice$Params, context?: HttpContext): Observable<StrictHttpResponse<InvoiceDto | null>> {
    return updateInvoice(this.http, this.rootUrl, params, context);
  }

  /**
   * Update an invoice.
   *
   * Updates an existing invoice record.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateInvoice$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateInvoice(params: UpdateInvoice$Params, context?: HttpContext): Observable<InvoiceDto | null> {
    return this.updateInvoice$Response(params, context).pipe(
      map((r: StrictHttpResponse<InvoiceDto | null>): InvoiceDto | null => r.body)
    );
  }

  /** Path part for operation `createInvoice()` */
  static readonly CreateInvoicePath = '/api/v1/invoice/create';

  /**
   * Create a new invoice.
   *
   * Creates a new invoice record.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createInvoice()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createInvoice$Response(params: CreateInvoice$Params, context?: HttpContext): Observable<StrictHttpResponse<InvoiceDto | null>> {
    return createInvoice(this.http, this.rootUrl, params, context);
  }

  /**
   * Create a new invoice.
   *
   * Creates a new invoice record.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createInvoice$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createInvoice(params: CreateInvoice$Params, context?: HttpContext): Observable<InvoiceDto | null> {
    return this.createInvoice$Response(params, context).pipe(
      map((r: StrictHttpResponse<InvoiceDto | null>): InvoiceDto | null => r.body)
    );
  }

  /** Path part for operation `attachMultipartFileToInvoice()` */
  static readonly AttachMultipartFileToInvoicePath = '/api/v1/invoice/attach-multipart-file/{invoiceId}';

  /**
   * Attach multipart file to invoice.
   *
   * Attaches a file to an existing invoice using Feign client.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `attachMultipartFileToInvoice()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  attachMultipartFileToInvoice$Response(params: AttachMultipartFileToInvoice$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return attachMultipartFileToInvoice(this.http, this.rootUrl, params, context);
  }

  /**
   * Attach multipart file to invoice.
   *
   * Attaches a file to an existing invoice using Feign client.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `attachMultipartFileToInvoice$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  attachMultipartFileToInvoice(params: AttachMultipartFileToInvoice$Params, context?: HttpContext): Observable<void> {
    return this.attachMultipartFileToInvoice$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `attachBase64FileToInvoice()` */
  static readonly AttachBase64FileToInvoicePath = '/api/v1/invoice/attach-base64-file/{invoiceId}';

  /**
   * Attach base64 file to invoice.
   *
   * Attaches a base64 encoded file to an invoice.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `attachBase64FileToInvoice()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  attachBase64FileToInvoice$Response(params: AttachBase64FileToInvoice$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return attachBase64FileToInvoice(this.http, this.rootUrl, params, context);
  }

  /**
   * Attach base64 file to invoice.
   *
   * Attaches a base64 encoded file to an invoice.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `attachBase64FileToInvoice$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  attachBase64FileToInvoice(params: AttachBase64FileToInvoice$Params, context?: HttpContext): Observable<void> {
    return this.attachBase64FileToInvoice$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `getInvoiceById()` */
  static readonly GetInvoiceByIdPath = '/api/v1/invoice/{id}';

  /**
   * Get an invoice by ID.
   *
   * Fetches an invoice by its unique identifier.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getInvoiceById()` instead.
   *
   * This method doesn't expect any request body.
   */
  getInvoiceById$Response(params: GetInvoiceById$Params, context?: HttpContext): Observable<StrictHttpResponse<InvoiceDto | null>> {
    return getInvoiceById(this.http, this.rootUrl, params, context);
  }

  /**
   * Get an invoice by ID.
   *
   * Fetches an invoice by its unique identifier.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getInvoiceById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getInvoiceById(params: GetInvoiceById$Params, context?: HttpContext): Observable<InvoiceDto | null> {
    return this.getInvoiceById$Response(params, context).pipe(
      map((r: StrictHttpResponse<InvoiceDto | null>): InvoiceDto | null => r.body)
    );
  }

  /** Path part for operation `deleteInvoice()` */
  static readonly DeleteInvoicePath = '/api/v1/invoice/{id}';

  /**
   * Delete an invoice.
   *
   * Deletes an invoice by its ID.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteInvoice()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteInvoice$Response(params: DeleteInvoice$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return deleteInvoice(this.http, this.rootUrl, params, context);
  }

  /**
   * Delete an invoice.
   *
   * Deletes an invoice by its ID.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteInvoice$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteInvoice(params: DeleteInvoice$Params, context?: HttpContext): Observable<void> {
    return this.deleteInvoice$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

  /** Path part for operation `getAllInvoices()` */
  static readonly GetAllInvoicesPath = '/api/v1/invoice/all';

  /**
   * Get all invoices.
   *
   * Fetches a paginated list of all invoices.
   *
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllInvoices()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllInvoices$Response(params: GetAllInvoices$Params, context?: HttpContext): Observable<StrictHttpResponse<CustomPageDtoObject>> {
    return getAllInvoices(this.http, this.rootUrl, params, context);
  }

  /**
   * Get all invoices.
   *
   * Fetches a paginated list of all invoices.
   *
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllInvoices$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllInvoices(params: GetAllInvoices$Params, context?: HttpContext): Observable<CustomPageDtoObject> {
    return this.getAllInvoices$Response(params, context).pipe(
      map((r: StrictHttpResponse<CustomPageDtoObject>): CustomPageDtoObject => r.body)
    );
  }

}
