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
import { Http } from '@angular/http';

@Component({
  selector: 'app-game-window',
  templateUrl: './game-window.component.html',
  styleUrls: ['./game-window.component.css']
})
export class GameWindowComponent implements OnInit {


  public ships: Ship[] = new Array<Ship>();


  constructor(private http: Http) { 
    this.http.get('http://www.mocky.io/v2/5babd5cb310000550065455a').subscribe((res) => {
      let result = res.json() as GameResponse;
      this.loadShips(result);
      this.renderShips();
    });
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
      console.log(this.ships[i]);
    }
  }

  ngOnInit() {
  }

  onCellClicked(event: Cell) {
    window.alert("FROM GAME WINDOW Row: " + event.row + "   Col: " + event.col);
  }

}

interface Cell {
  row: number;
  col: number;
  index: number;
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


