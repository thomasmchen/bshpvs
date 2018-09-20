import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-game-window',
  templateUrl: './game-window.component.html',
  styleUrls: ['./game-window.component.css']
})
export class GameWindowComponent implements OnInit {

  constructor() { 
    
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
