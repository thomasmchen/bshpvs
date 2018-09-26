import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DarkModeService } from '../settings/darkmode.service';

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
  darkMode: boolean;

  carrier : Ship = {identifier: 0, numSpaces: 5, spaces: new Array<Coordinate>()};
  cruiser: Ship = {identifier: 1, numSpaces: 4, spaces: new Array<Coordinate>()};
  submarine1: Ship = {identifier: 2, numSpaces: 3, spaces: new Array<Coordinate>()};
  submarine2: Ship = {identifier: 2, numSpaces: 3, spaces: new Array<Coordinate>()}
  destroyer : Ship = {identifier: 3, numSpaces: 2, spaces: new Array<Coordinate>()};

  message: string = "Place your carrier (4 spaces left)";

  temp: any = "";

  username: string = "";
  victoryMessage: string = "";

  constructor(public snackbar: MatSnackBar, private router: Router, private dm: DarkModeService) { }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
    const body = document.getElementsByTagName('mat-card')[0];
    if(this.darkMode) {
      body.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
    }
  }

  onCellClicked(event: Cell) {
    var total : number = this.carrier.numSpaces + this.cruiser.numSpaces + this.destroyer.numSpaces + this.submarine1.numSpaces + this.submarine2.numSpaces;
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
    } else if (this.placementCounter < this.submarine1.numSpaces + this.cruiser.numSpaces + this.carrier.numSpaces) {
      if(!this.checkValidMove(event, this.submarine1))
        return;
      this.submarine1.spaces.push({x: event.col, y: event.row});
      this.message = "Place your 1st submarine (3 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces + this.cruiser.numSpaces + this.submarine1.numSpaces) {
        this.message = "Place your 2nd submarine (2 spaces)";
      }
    } else if (this.placementCounter < total - this.destroyer.numSpaces) {
      if(!this.checkValidMove(event, this.submarine2))
        return;
      this.submarine2.spaces.push({x: event.col, y: event.row});
      this.message = "Place your 2nd submarine (3 spaces)";
      this.placementCounter++;
      if (this.placementCounter == total - this.destroyer.numSpaces) {
        this.message = "Place your destroyer (2 spaces)";
      }
    }
    else if (this.placementCounter < total){
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
    var tempShip = ship;
    let colinearTest = this.checkShipColinear(ship, {x: event.col, y: event.row});
    if (!colinearTest && ship.spaces.length >= 2) {
      window.alert('colinear test');
      return false;
    }
    

    var coordinates = ship.spaces;
    var flag = false;
    for (let c of coordinates) {
      if (c.x == event.col && c.y == event.row) {
        window.alert('here');
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

  checkShipColinear(ship: Ship, coordinate: Coordinate) {
    if (ship.spaces.length < 2) {
      return true;
    } else {
      var tempSpaces = new Array<Coordinate>();

      // duplicate the ship array
      for (var i = 0; i < ship.spaces.length; i++) {
        let tempSpace = { x: ship.spaces[i].x, y: ship.spaces[i].y};
        tempSpaces.push(tempSpace);
      }

      // add new cadidate
      tempSpaces.push(coordinate);
      if (tempSpaces.length <= 2) {
        return true;
      }

      var lastSlope = 100;

      for (var i = 1; i < tempSpaces.length; i++) {
        let n = tempSpaces[i];
        let c = tempSpaces[i-1];
        let slope = (n.y - c.y) / (n.x - c.x);
        if (lastSlope == 100) {
          lastSlope = slope;
        } else if (lastSlope != slope) {
          return false;
        }
      }
      return true;
    }
  }


  onSubmit() {
    if (this.victoryMessage == "" || this.username == "") {
      this.snackbar.open("Enter a victory message/username", 'Ok', {
        duration: 2000
      });
    } else {

      let reqs = new Array<ShipReq>();
      let ships = new Array<Ship>();
      ships.push(this.carrier, this.cruiser, this.submarine1, this.submarine2, this.destroyer);
      for (var i = 0; i < ships.length; i++) {
        let ship = ships[i];
        var req : ShipReq = {
          identifier: ship.identifier, 
          numSpaces: ship.numSpaces,
          firstSpace: ship.spaces[0],
          lastSpace: ship.spaces[ship.spaces.length - 1]
        };

        reqs.push(req);
      }

      let request : GameRequest = {
        userId: 0,
        userName: this.username,
        victoryMessage: this.victoryMessage,
        ships: reqs
      };
      //this.router.navigateByUrl('/gameWindow');

      let r = JSON.stringify(request);

      console.log(r);
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

interface ShipReq {
  identifier: number,
  numSpaces: number,
  firstSpace: Coordinate,
  lastSpace: Coordinate
}

interface GameRequest {
  userId: number,
  userName: string,
  victoryMessage: string,
  ships: ShipReq[]
}