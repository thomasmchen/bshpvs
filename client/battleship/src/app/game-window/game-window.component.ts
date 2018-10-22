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




import { Component, OnInit } from '@angular/core';
import { timer } from 'rxjs';

import { DarkModeService } from '../settings/darkmode.service';
import { Http, Jsonp } from '@angular/http';
import { WebService } from '../web.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-game-window',
  templateUrl: './game-window.component.html',
  styleUrls: ['./game-window.component.css']
})
export class GameWindowComponent implements OnInit {

  darkMode:boolean;
  timer:boolean;
  won: boolean = false;
    

  public ships: Ship[] = new Array<Ship>();

  gridSize: number = 10;

  lastX : number = 0;
  lastY : number = 0;


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

  
    let that = this;
    this.stomp.stompClient.subscribe('/topic/turnResponse', (res) => {
      console.log(res);
      let r = JSON.parse(res.body) as AttackResponse;
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
    this.turnShipColor(0, 0);
  }

  endGame() {
    this.won = true;
    this.stomp.stompClient.disconnect();
  }

  ngOnDestroy() {
    console.log("called");
    this.stomp.stompClient.disconnect();
    this.ships = [];
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
    if (!this.won) {
      this.makePlayerAttack(event.col, event.row);
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


    this.stomp.sendMove(j);

    console.log(request);
  }

  makePlayerMove(_x, _y, _direction) {
    let request : MoveRequest = {
      x: _x,
      y: _y, 
      direction: _direction
    }

    console.log(request);
  }

  turnAllShipsColor(color) {

  }

  turnShipColor(color, id) {
    for (var i = 0; i < this.ships.length; i++) {
      console.log(this.ships[i].identifier);
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
  x: number,
  y: number,
  direction: number
}

interface EndGameResponse {
  won: string,
  victoryMessage: string
}


