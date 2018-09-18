import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-game-controls',
  templateUrl: './game-controls.component.html',
  styleUrls: ['./game-controls.component.css']
})
export class GameControlsComponent implements OnInit {

  message : string = 'Please place your first battleship';
  constructor() { }

  ngOnInit() {
  }

  onPressed() {
    
  }

}
