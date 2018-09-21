import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ShipAnimationService {

  constructor(public name: string, public state = 'inactive') { }

  toggleState() {
    this.state = this.state === 'active' ? 'inactive' : 'active';
  }
  
}
