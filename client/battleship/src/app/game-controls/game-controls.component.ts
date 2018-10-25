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
    this.message = 'Select a ship you would like to move. If the ship is hit (contains a black cell), your move will be invalid and you will waste a turn.';
    this.event.emit('move');
  }

  onForwardPressed() {
    this.event.emit('forward');
  }

  onBackwardPressed() {
    this.event.emit('backward');
  }

  showDirectionalButtons() {
    document.getElementById('forward').style.visibility = 'visible';
    document.getElementById('backward').style.visibility = 'visible';
  }

  hideDirectionalButtons() {
    document.getElementById('forward').style.visibility = 'hidden';
    document.getElementById('backward').style.visibility = 'hidden';
  }

  hideMoveButton() {
    document.getElementById('move').style.visibility = 'hidden';
  }

  hideSurrenderButton() {
    document.getElementById('surrender').style.visibility = 'hidden';
  }

  showMoveButton() {
    document.getElementById('move').style.visibility = 'visible';
  }

  showSurrenderButton() {
    document.getElementById('surrender').style.visibility = 'visible';
  }

  setMessage(mes) {
    this.message = mes;
  }

}
