import { Component, OnInit } from '@angular/core';
import { timer } from 'rxjs';

import { DarkModeService } from '../settings/darkmode.service';

@Component({
  selector: 'app-game-window',
  templateUrl: './game-window.component.html',
  styleUrls: ['./game-window.component.css']
})
export class GameWindowComponent implements OnInit {

  darkMode:boolean;
  timer:boolean;

  constructor(private dm: DarkModeService) { 
    
  }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
    this.dm.currentTimer.subscribe(timer => this.timer = timer);
    var tmr = timer(0,1000);
    tmr.subscribe(
      function(x) {
        var tst = document.querySelector(".timerContainer");
        tst.innerHTML = ""+x;
      }
    );
    const body = document.getElementsByTagName('mat-card')[0];
    const timerContainer = document.getElementsByClassName('timerContainer')[0];
    if(this.darkMode) {
      body.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
    }
    if(this.timer) {
      timerContainer.classList.remove('hidden');
    } else {
      timerContainer.classList.add('hidden');
    }
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
