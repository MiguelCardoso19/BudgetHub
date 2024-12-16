import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class ErrorHandlingService {
  handleError(err: any): string[] {
    const errorMsg: string[] = [];

    if (err.error instanceof Blob) {
      err.error.text().then((errorText: string) => {
        try {
          const parsedError = JSON.parse(errorText);
          if (parsedError) {
            for (let key in parsedError) {
              if (parsedError.hasOwnProperty(key)) {
                if (key !== 'status' && key !== 'errorCode') {
                  errorMsg.push(parsedError[key]);
                }
              }
            }
          }
        } catch (e) {
          errorMsg.push(errorText);
        }
      });
    } else {
      errorMsg.push(err.error);
    }
    return errorMsg;
  }
}
