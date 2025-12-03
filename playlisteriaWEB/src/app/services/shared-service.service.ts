import { ChangeDetectorRef, Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SharedServiceService {
  _spinnerModalActive = false;
  constructor() { }

  get spinnerModal(): boolean {
    console.log("get");
    return this._spinnerModalActive;
  }

  set spinnerModal(value: boolean) {
    console.log(value);
    this._spinnerModalActive = value;
  }

}
