import { DOCUMENT } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DarkModeService } from '../settings/darkmode.service';
import { WebService } from '../web.service';
import { Config } from 'protractor';
import { AuthService } from '../auth.service';
import {FormControl} from '@angular/forms';




@Component({
  selector: 'app-new-game-menu',
  templateUrl: './new-game-menu.component.html',
  styleUrls: ['./new-game-menu.component.css']
})
export class NewGameMenuComponent implements OnInit {

  selectCarrier: boolean = false;
  selectbattleship: boolean = false;
  selectSubmarine: boolean = false;
  selectDestroyer: boolean = false;
  placementCounter: number = 0;
  darkMode: boolean;
  user_id:string;

  carrier : Ship = {identifier: 0, numSpaces: 5, spaces: new Array<Coordinate>()};
  battleship: Ship = {identifier: 1, numSpaces: 4, spaces: new Array<Coordinate>()};
  cruiser: Ship = {identifier: 2, numSpaces: 3, spaces: new Array<Coordinate>()};
  submarine: Ship = {identifier: 3, numSpaces: 3, spaces: new Array<Coordinate>()}
  destroyer : Ship = {identifier: 4, numSpaces: 2, spaces: new Array<Coordinate>()};

  availableAI : string[] = ["Normal", "Hunter"];
  availableNumOpponents : string[] = ["1", "2", "3"];
  message: string = "Place your carrier (5 spaces left)";
  temp: any = "";

  username: string = "";
  victoryMessage: string = "";
  selectedAI: string = this.availableAI[0];
  numberOfOpponents: string = '1';

  constructor(public snackbar: MatSnackBar, private stomp: WebService, private router: Router, private dm: DarkModeService, private auth: AuthService, @Inject(DOCUMENT) private document: any) { 
    // initialize connection to backend
    stomp.reinitializeConnection();  
  }

  ngOnInit() {
    if (this.document.exitFullscreen) {
      this.document.exitFullscreen();
    } else if (this.document.mozCancelFullScreen) {
      /* Firefox */
      this.document.mozCancelFullScreen();
    } else if (this.document.webkitExitFullscreen) {
      /* Chrome, Safari and Opera */
      this.document.webkitExitFullscreen();
    } else if (this.document.msExitFullscreen) {
      /* IE/Edge */
      this.document.msExitFullscreen();
    }
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
    const body = document.getElementsByTagName('mat-card')[0];
    if(this.darkMode) {
      body.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
    }
  }

  onCellClicked(event: Cell) {
    if (this.placementCounter == 0) {
      this.stomp.stompClient.subscribe('/topic/confirmPlacement', (res) => {
        this.stomp.setConnected();
        this.router.navigateByUrl('/gameWindow');
      });
    }
    

    var total : number = this.carrier.numSpaces + this.battleship.numSpaces + this.destroyer.numSpaces + this.cruiser.numSpaces + this.submarine.numSpaces;
    if (this.placementCounter < this.carrier.numSpaces) {
      if(!this.checkValidMove(event, this.carrier))
        return;
      this.carrier.spaces.push({x: event.col, y: event.row});
      this.message = "Place your carrier (5 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces) {
        this.message = "Place your battleship (4 spaces)";
      }
    } else if (this.placementCounter < this.battleship.numSpaces + this.carrier.numSpaces) {
      if(!this.checkValidMove(event, this.battleship))
        return;
      this.battleship.spaces.push({x: event.col, y: event.row});
      this.message = "Place your battleship (4 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces + this.battleship.numSpaces) {
        this.message = "Place your cruiser (3 spaces)";
      }
    } else if (this.placementCounter < this.cruiser.numSpaces + this.battleship.numSpaces + this.carrier.numSpaces) {
      if(!this.checkValidMove(event, this.cruiser))
        return;
      this.cruiser.spaces.push({x: event.col, y: event.row});
      this.message = "Place your cruiser (3 spaces)";
      this.placementCounter++;
      if (this.placementCounter == this.carrier.numSpaces + this.battleship.numSpaces + this.cruiser.numSpaces) {
        this.message = "Place your submarine (3 spaces)";
      }
    } else if (this.placementCounter < total - this.destroyer.numSpaces) {
      if(!this.checkValidMove(event, this.submarine))
        return;
      this.submarine.spaces.push({x: event.col, y: event.row});
      this.message = "Place your submarine (3 spaces)";
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
      return false;
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

  checkShipDiagnol(ship: Ship, coordinate: Coordinate) {
    if (ship.spaces.length < 2) {
      return true;
    } else {
      var tempSpaces = new Array<Coordinate>();

      for (var i = 0; i < ship.spaces.length; i++) {
        let tempSpace = {x : ship.spaces[i].x, y: ship.spaces[i].y};
        tempSpaces.push(tempSpace);
      }

      tempSpaces.push(coordinate);
    }
  }


  // Action where view sends information back to the backend
  onSubmit() {
    if (this.victoryMessage == "" || this.username == "") {
      this.snackbar.open("Enter a victory message/username", 'Ok', {
        duration: 2000
      });
    } else {

      let reqs = new Array<ShipReq>();
      let ships = new Array<Ship>();
      ships.push(this.carrier, this.battleship, this.cruiser, this.submarine, this.destroyer);
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

      this.auth.currentid.subscribe(user_id => this.user_id = user_id);
      let sendFormat = "{\"id\":\""+this.user_id+"\"}";
      //alert(sendFormat);

      this.stomp.sendID(sendFormat);

      let request : GameRequest = {
        userId: this.user_id,
        userName: this.username,
        victoryMessage: this.victoryMessage,
        ships: reqs,
        selectedAI: this.selectedAI,
        numberOfOpponents: this.numberOfOpponents
      };
      //this.router.navigateByUrl('/gameWindow');

     
      let r = JSON.stringify(request);
      this.stomp.sendMessage(r);
      console.log(r);
    }
  }

}

interface Ship {
  // 0 is carrier
  // 1 is battleship
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
  index: number,
  id: string
}

interface ShipReq {
  identifier: number,
  numSpaces: number,
  firstSpace: Coordinate,
  lastSpace: Coordinate
}

interface GameRequest {
  userId: string,
  userName: string,
  victoryMessage: string,
  ships: ShipReq[],
  selectedAI: string,
  numberOfOpponents: string
}
