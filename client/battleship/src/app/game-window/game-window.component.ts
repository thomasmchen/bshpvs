// Sample response for getting placed ships

/*
{
  "userId":0,
  "userName":"test",
  "victoryMessage":"test",
  "ships":[
      {"identifier":0,"numSpaces":5,"spaces":[{"x":0,"y":0},{"x":1,"y":0},{"x":2,"y":0},{"x":3,"y":0},{"x":4,"y":0}]},
      {"identifier":1,"numSpaces":5,"spaces":[{"x":0,"y":1},{"x":1,"y":1},{"x":2,"y":1},{"x":3,"y":1}]},
      {"identifier":2,"numSpaces":5,"spaces":[{"x":0,"y":2},{"x":1,"y":2},{"x":2,"y":2}]},
      {"identifier":2,"numSpaces":5,"spaces":[{"x":0,"y":3},{"x":1,"y":3},{"x":2,"y":3}]},
      {"identifier":3,"numSpaces":5,"spaces":[{"x":0,"y":4},{"x":1,"y":4}]}
  ]
}
*/




import { Component, OnInit, ViewChild } from '@angular/core';
import { timer } from 'rxjs';

import { DarkModeService } from '../settings/darkmode.service';
import { Http, Jsonp } from '@angular/http';
import { WebService } from '../web.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { GameControlsComponent } from '../game-controls/game-controls.component';

@Component({
  selector: 'app-game-window',
  templateUrl: './game-window.component.html',
  styleUrls: ['./game-window.component.css']
})
export class GameWindowComponent implements OnInit {

  @ViewChild(GameControlsComponent) gameControls;
  darkMode:boolean;
  timer:boolean;
  won: boolean = false;
    

  public ships: Ship[] = new Array<Ship>();

  gridSize: number = 10;

  lastX : number = 0;
  lastY : number = 0;

  movingShip: boolean = false;
  selectedShipId: number = -1;
  attackResponses: AttackResponse[] = [];


  constructor(private http: Http, private dm: DarkModeService, private stomp: WebService, public snackBar: MatSnackBar) { 
    /*this.http.get('http://www.mocky.io/v2/5babd5cb310000550065455a').subscribe((res) => {
      let result = res.json() as GameResponse;
      this.loadShips(result);
      this.renderShips();
    });*/

    if (this.stomp.stompClient) {
      console.log("Connected to server in game window");
    } else {
      console.log("Not connected");
    }


    this.stomp.stompClient.subscribe('/topic/windowInitResponse', (res) => {
      let r = JSON.parse(res.body) as GameResponse;
      console.log(r);
      this.loadShips(r);
      this.renderShips();
    });

    this.stomp.stompClient.subscribe('/topic/moveResponse', (res) => {
      this.clearBoard();
      let r = JSON.parse(res.body) as MoveResponse;
      if (r.shipId == -1) {
        this.gameControls.setMessage("Invalid move. Lost turn.");
      } else {
        for (var i = 0; i < this.ships.length; i++) {
          if (this.ships[i].identifier == r.shipId) {
            this.ships[i].spaces = r.spaces;
            break;
          }
        }
      }
      
      this.renderShips();
      this.movingShip = false;
      this.selectedShipId = -1;
      this.gameControls.hideDirectionalButtons();
      this.gameControls.showMoveButton();
      this.gameControls.showSurrenderButton();
      this.gameControls.setMessage("Select a cell to attack");
      this.makePlayerAttack(-1, -1);
    });

  
    let that = this;
    this.stomp.stompClient.subscribe('/topic/turnResponse', (res) => {
      console.log(res);
      let r = JSON.parse(res.body) as AttackResponse;
      this.attackResponses.push(r);
      if (r.yourMove.substring(0, 2) == "hit") {
        this.markEnemyGrid(this.lastX, this.lastY, true);
      } else if (r.yourMove == "water") {
        this.markEnemyGrid(this.lastX, this.lastY, false);
      } else if (r.yourMove.substring(0,3) == "sunk") {
        this.markEnemyGrid(this.lastX, this.lastY, true);
      } else if (r.yourMove.substring(0, 2) == "won") {
        window.alert("You won!")
        this.markEnemyGrid(this.lastX, this.lastY, true);
        that.won = true;
        that.endGame();

      } else if (r.yourMove == "move") {
        
      } else {
        this.markEnemyGrid(this.lastX, this.lastY, false);
      }

      

      if (r.theirMove.substring(0, 3) == "hit") {
        this.markUserGrid(r.x, r.y, true);
      
      } else if (r.theirMove.substring(0, 4) == "sunk") {
        this.markUserGrid(r.x, r.y, true);
      } else if (r.theirMove.substring(0, 3) == "won") {
        window.alert("Enemy won!")
        this.markUserGrid(r.x, r.y, true);
        that.endGame();
      } else {
        this.markUserGrid(r.x, r.y, false);
      }

      this.snackBar.open(r.message, 'Ok', {
        duration: 2000
      });
    });
    this.stomp.sendGameWindowInit();

  }

  handleSignal(event: string) {
    console.log(event);
    this.movingShip = true;
    if (event == "move") {
      this.turnAllShipsColor("blue");
    } else if (event == "forward") {
      if (this.selectedShipId == -1) {
        console.log("Something concerning just happend in handleSignal()");
      }
      this.makePlayerMove(this.selectedShipId, 'forward');
    } else if (event == "backward") {
      console.log("Player wants to move ship backwards.")
      this.makePlayerMove(this.selectedShipId, 'backward');
    }
  }

  endGame() {
    this.won = true;
    this.stomp.stompClient.disconnect();
    this.attackResponses = [];
    this.ships = [];
  }

  ngOnDestroy() {
    console.log("called");
    this.stomp.stompClient.disconnect();
    this.ships = []; this.attackResponses = [];
  }

  markUserGrid(x, y, hit) {
    var id = y * this.gridSize + x;
    if (hit) {
      document.getElementById("user_"+id).style.backgroundColor = "black";
    } else {
      document.getElementById("user_"+id).style.backgroundColor = "yellow";
    }
  }

  markEnemyGrid(x, y, hit) {
    var id = y * this.gridSize + x;
    if (hit) {
      document.getElementById("enemy_"+id).style.backgroundColor = "black";
    } else {
      document.getElementById("enemy_"+id).style.backgroundColor = "yellow";

    }
  }

  loadShips(result: GameResponse) {
    this.ships = [];
    let _ships = result.ships;
    for (var i = 0; i < result.ships.length; i++) {
      var curr = _ships[i];
      let id = curr.identifier;
      let numSpaces = curr.numSpaces;
      let spaces = curr.spaces;
      var newShip : Ship =  {
        "identifier": id,
        "numSpaces": numSpaces,
        "spaces": spaces
      };

      this.ships.push(newShip);
    }
  }

  renderShips() {
    for (var i = 0; i < this.ships.length; i++) {
      for (var j = 0; j < this.ships[i].spaces.length; j++) {
        
        let x = this.ships[i].spaces[j].x;
        let y = this.ships[i].spaces[j].y;
        var id = y * this.gridSize + x;
        document.getElementById("user_"+id).style.backgroundColor = "red";
      }
    }

    this.renderAllAttackResponsesRecieved();
  }

  clearBoard() {
    this.turnAllShipsColor("lightblue");
  }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
    this.dm.currentTimer.subscribe(timer => this.timer = timer);
    var tmr = timer(0,1000);
    tmr.subscribe(
      function(x) {
        var tst = document.querySelector(".timerContainer");
        var sec = x%60;
        var min = Math.floor(x/60);
        if(sec < 10) {
          tst.innerHTML = min+":0"+sec;
        } else {
          tst.innerHTML = min+":"+sec;
        }
      }
    );
    const body = document.getElementsByTagName('mat-card')[0];
    const timerContainer = document.getElementsByClassName('timerContainer')[0];
    const timerBody = document.getElementsByTagName('mat-card')[1];
    if(this.darkMode) {
      body.classList.add('darkMode');
      timerBody.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
      timerBody.classList.remove('darkMode');
    }
    if(this.timer) {
      timerContainer.classList.remove('hidden');
      timerBody.classList.remove('hidden');
    } else {
      timerContainer.classList.add('hidden');
      timerBody.classList.add('hidden');
    }
  }

  onCellClicked(event: Cell) {
    if (!this.won && !this.movingShip) {
      this.makePlayerAttack(event.col, event.row);
    } else if (this.movingShip && this.selectedShipId == -1) {
      var shipId = 0;
      for (var i = 0; i < this.ships.length; i++) {
        var s = this.ships[i];
        if (this.shipContainsPoint(s, event.col, event.row)) {
          let st = s.spaces[0];
          let end = s.spaces[s.spaces.length - 1];
          // chcek to see if the ship is vertical or horizontal
          this.turnShipColor("yellow", s.identifier);

          if (st.x == end.x) {
            this.changeCellColor(s.spaces[0].x, s.spaces[0].y, "darkred", "user_");
            this.changeCellColor(s.spaces[s.numSpaces - 1].x, s.spaces[s.numSpaces - 1].y, "pink", "user_");
          } else {
            this.changeCellColor(s.spaces[0].x, s.spaces[0].y, "pink", "user_");
            this.changeCellColor(s.spaces[s.numSpaces - 1].x, s.spaces[s.numSpaces - 1].y, "darkred", "user_");
          }
          this.gameControls.setMessage("Move the selected ship forward or backward. Pink represents the back of the boat, while dark red represents the front of the boat.");
          this.selectedShipId = s.identifier;
          break;
        }
      }


      this.turnShipColor(shipId, "black");
      this.gameControls.showDirectionalButtons();
      this.gameControls.hideMoveButton();
      this.gameControls.hideSurrenderButton();

    }
  }

  makePlayerAttack(_x, _y) {
    this.lastX = _x;
    this.lastY = _y;
    let request : AttackRequest = {
      x : _x,
      y : _y
    }

    let j = JSON.stringify(request);


    this.stomp.sendAttack(j);

    console.log(request);
  }

  makePlayerMove(id, _direction) {
    let request : MoveRequest = {
      shipId: id,
      direction: _direction
    }
    let j = JSON.stringify(request);

    this.stomp.sendMove(j);

  }

  turnAllShipsColor(color) {
    for (var i = 0; i < this.ships.length; i++) {
      this.turnShipColor(color, this.ships[i].identifier);
    }
  }

  shipContainsPoint(s: Ship, x: number, y: number) {
    for (var i = 0; i < s.spaces.length; i++) {
      let point = s.spaces[i];
      if (point.x == x && point.y == y) {
        return true;
      }
    }

    return false;
  }

  turnShipColor(color, id) {
    for (var i = 0; i < this.ships.length; i++) {
      if (i == id) {
        let ship = this.ships[i];
        for (var j = 0; j < ship.spaces.length; j++) {
          var x = ship.spaces[j].x;
          var y = ship.spaces[j].y;
          var domId = y * this.gridSize + x;
          document.getElementById("user_"+domId).style.backgroundColor = color;
        }
      }
    }

    this.renderAllAttackResponsesRecieved();

  }

  changeCellColor(x, y, color, prefix) {
    var id = y * this.gridSize + x;
    document.getElementById(prefix+id).style.backgroundColor = color;

  }

  renderAllAttackResponsesRecieved() {
    for (var i = 0; i < this.attackResponses.length; i++) {
      console.log("iterate");
      let r = this.attackResponses[i];
      if (r.theirMove.substring(0,3) == "hit") {
        this.markUserGrid(r.x, r.y, true);
      } else {
        this.markUserGrid(r.x, r.y, false);
      }
    }
  }

  

}

interface Cell {
  row: number;
  col: number;
  index: string;
}

interface Ship {
  identifier: number,
  numSpaces: number,
  spaces: Coordinate[]
}

interface AttackResponse {
  yourMove: string,
  theirMove: string,
  message: string,
  x: number,
  y: number
}

interface GameResponse {
  userId: number,
  userName: string,
  victoryMessage: string,
  ships: Ship[]
}

interface Coordinate {
  x: number,
  y: number
}

interface AttackRequest {
  x: number,
  y: number
}

interface MoveRequest {
  shipId: number,
  direction: String
}

interface MoveResponse {
  shipId: number,
  spaces: Coordinate[]
}

interface EndGameResponse {
  won: string,
  victoryMessage: string
}


