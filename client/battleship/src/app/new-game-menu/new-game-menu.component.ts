import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-game-menu',
  templateUrl: './new-game-menu.component.html',
  styleUrls: ['./new-game-menu.component.css']
})
export class NewGameMenuComponent implements OnInit {

  selectCarrier: boolean = false;
  selectCruiser: boolean = false;
  selectSubmarine: boolean = false;
  selectDestroyer: boolean = false;
  placementCounter: number = 0;

  carrier : Ship = {identifier: 0, numSpaces: 5, spaces: new Array<Coordinate>()};
  cruiser: Ship = {identifier: 1, numSpaces: 4, spaces: new Array<Coordinate>()};
  submarine: Ship = {identifier: 2, numSpaces: 3, spaces: new Array<Coordinate>()};
  destroyer : Ship = {identifier: 3, numSpaces: 2, spaces: new Array<Coordinate>()};

  message: string = "Place your carrier (4 spaces left)";


  username: string = "";
  victoryMessage: string = "";

  constructor(public snackbar: MatSnackBar, private router: Router) { }

  ngOnInit() {
  }

  onCellClicked(event: Cell) {
    var total : number = this.carrier.numSpaces + this.cruiser.numSpaces + this.destroyer.numSpaces + this.submarine.numSpaces;
    if (this.placementCounter < this.carrier.numSpaces) {
      if(!this.checkValidMove(event, this.carrier))
        return;
      this.carrier.spaces.push({x: event.col, y: event.row});
      this.message = "Place your carrier (5 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces) {
        this.message = "Place your cruiser (4 spaces)";
      }
    } else if (this.placementCounter < this.cruiser.numSpaces + this.carrier.numSpaces) {
      if(!this.checkValidMove(event, this.cruiser))
        return;
      this.cruiser.spaces.push({x: event.col, y: event.row});
      this.message = "Place your cruiser (4 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces + this.cruiser.numSpaces) {
        this.message = "Place your submarine (3 spaces)";
      }
    } else if (this.placementCounter < this.submarine.numSpaces + this.cruiser.numSpaces + this.carrier.numSpaces) {
      if(!this.checkValidMove(event, this.submarine))
        return;
      this.submarine.spaces.push({x: event.col, y: event.row});
      this.message = "Place your submarine (3 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces + this.cruiser.numSpaces + this.submarine.numSpaces) {
        this.message = "Place your destroyer (2 spaces)";
      }
    } else if (this.placementCounter < total){
      if(!this.checkValidMove(event, this.destroyer))
        return;
      this.destroyer.spaces.push({x: event.col, y: event.row});
      this.message = "Place your destroyer (2 spaces)";
      this.placementCounter++;
      if (this.placementCounter == total) {
        this.message = "All ships placed";
        document.getElementById('submit').removeAttribute('disabled');
        document.getElementById(event.index + '').style.backgroundColor = "red";
      }
    }
    if (this.placementCounter < total) {
      document.getElementById(event.index + '').style.backgroundColor = "red";
    }
  }

  checkValidMove(event: Cell, ship: Ship) {
    if (ship.spaces.length == 0) {
      return true;
    }
    var coordinates = ship.spaces;
    var flag = false;
    for (let c of coordinates) {
      if (c.x == event.col && c.y == event.row) {
        return false;
      }
      if (Math.abs(c.x - event.col) < 2 && Math.abs(c.y - event.row) < 2) {
        flag = true;
      }
    }

    if (!flag) {
      this.snackbar.open("Invalid space entered.", 'Ok', {
        duration: 2000
      });
    }
    return flag;
  }

  onSubmit() {
    if (this.victoryMessage == "" || this.username == "") {
      this.snackbar.open("Enter a victory message/username", 'Ok', {
        duration: 2000
      });
    } else {
      this.router.navigateByUrl('/gameWindow');
    }
  }
}

interface Ship {
  // 0 is carrier
  // 1 is cruiser
  // 2 is submarine
  // 3 is destroyer
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
