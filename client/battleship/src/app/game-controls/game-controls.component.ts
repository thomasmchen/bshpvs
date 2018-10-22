import { Component, OnInit, Output } from '@angular/core';
import { EventEmitter } from '@angular/core';

@Component({
  selector: 'app-game-controls',
  templateUrl: './game-controls.component.html',
  styleUrls: ['./game-controls.component.css']
})
export class GameControlsComponent implements OnInit {

  message : string = 'Please place your first battleship';
  constructor() { }

  @Output() event = new EventEmitter();

  ngOnInit() {
  }

  onPressed() {
    
  }

  onMovedPressed() {
    document.getElementById('message').style.fontWeight = '900';
    this.message = 'Select a ship you would like to move';
    this.event.emit('move');
  }

}
