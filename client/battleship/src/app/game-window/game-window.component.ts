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
import { Http } from '@angular/http';
import { WebService } from '../web.service';

@Component({
  selector: 'app-game-window',
  templateUrl: './game-window.component.html',
  styleUrls: ['./game-window.component.css']
})
export class GameWindowComponent implements OnInit {

  darkMode:boolean;
  timer:boolean;
    

  public ships: Ship[] = new Array<Ship>();

  gridSize: number = 10;


  constructor(private http: Http, private dm: DarkModeService, private stomp: WebService) { 
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
    this.stomp.sendGameWindowInit();




  }

  loadShips(result: GameResponse) {
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
    this.makePlayerMove(event.row, event.col, 0);
  }

  makePlayerAttack(_x, _y) {
    let request : AttackRequest = {
      enemy : 0,
      x : _x,
      y : _y
    }

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
  enemy: number,
  x: number,
  y: number
}

interface MoveRequest {
  x: number,
  y: number,
  direction: number
}


