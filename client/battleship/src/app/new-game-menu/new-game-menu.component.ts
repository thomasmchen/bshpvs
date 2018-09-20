import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-new-game-menu',
  templateUrl: './new-game-menu.component.html',
  styleUrls: ['./new-game-menu.component.css']
})
export class NewGameMenuComponent implements OnInit {

  selectB1: boolean = false;
  selectB2: boolean = false;
  selectC: boolean = false;
  selectAC: boolean = false;
  placementCounter: number = 0;

  battleShip1 : Ship = {identifier: 0, numSpaces: 3, spaces: new Array<Coordinate>()};
  battleShip2 : Ship = {identifier: 0, numSpaces: 3, spaces: new Array<Coordinate>()};
  cruiser: Ship = {identifier: 1, numSpaces: 2, spaces: new Array<Coordinate>()};
  carrier: Ship = {identifier: 2, numSpaces: 4, spaces: new Array<Coordinate>()};

  message: string = "Place your carrier (4 spaces left)";


  constructor() { }

  ngOnInit() {
  }

  onCellClicked(event: Cell) {
    var total : number = this.carrier.numSpaces + this.battleShip1.numSpaces + this.battleShip2.numSpaces + this.cruiser.numSpaces;
    if (this.placementCounter < this.carrier.numSpaces) {
      this.carrier.spaces.push({x: event.col, y: event.row});
      this.message = "Place your carrier (4 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces) {
        this.message = "Place your 1st battleship (3 spaces)";
      }
    } else if (this.placementCounter < this.battleShip1.numSpaces + this.carrier.numSpaces) {
      this.battleShip1.spaces.push({x: event.col, y: event.row});
      this.message = "Place your 1st battleship (3 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces + this.battleShip1.numSpaces) {
        this.message = "Place your 2nd battleship (3 spaces)";
      }
    } else if (this.placementCounter < this.battleShip2.numSpaces + this.battleShip1.numSpaces + this.carrier.numSpaces) {
      this.battleShip2.spaces.push({x: event.col, y: event.row});
      this.message = "Place your 2nd battleship (3 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces + this.battleShip1.numSpaces + this.battleShip2.numSpaces) {
        this.message = "Place your cruiser (2 spaces)";
      }
    } else if (this.placementCounter < total){
      this.cruiser.spaces.push({x: event.col, y: event.row});
      this.message = "Place your cruiser (2 spaces)";
      this.placementCounter++;
      if (this.placementCounter == total) {
        this.message = "All ships placed";
      }
    }
    if (this.placementCounter < total) {
      document.getElementById(event.index + '').style.backgroundColor = "red";
    }

  }

}

interface Ship {
  // 0 is battleship
  // 1 is cruiser
  // 2 is aircraft carrier
  identifier: number,
  numSpaces: number,
  spaces: Coordinate[]
}

interface Coordinate {
  x: number,
  y: number
}

interface Cell {
  row: number,
  col: number,
  index: number
}
