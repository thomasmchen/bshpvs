import { Component, OnInit } from '@angular/core';
import { DarkModeService } from './darkmode.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  darkMode:boolean;
  timer: boolean;
  constructor(private dm: DarkModeService) { }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
    this.dm.currentTimer.subscribe(timer => this.timer = timer);
    const body = document.getElementsByTagName('mat-card')[0];
    if(this.darkMode) {
      const slider: HTMLInputElement = <HTMLInputElement>document.getElementsByClassName('dmtoggle')[0];
      slider.checked = true;
      body.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
    }
    if(this.timer) {
      const slider: HTMLInputElement = <HTMLInputElement>document.getElementsByClassName('timerToggle')[0];
      slider.checked = true;
    }
  }

  toggleDarkMode() {
    this.dm.toggleDarkMode(!this.darkMode);
    const body = document.getElementsByTagName('mat-card')[0];
    if(this.darkMode) {
      body.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
    }
  }

  toggleTimer() {
   this.dm.toggleTimer(!this.timer);
  }

}
