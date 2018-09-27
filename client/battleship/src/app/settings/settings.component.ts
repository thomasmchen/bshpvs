import { Component, OnInit, ViewChild } from '@angular/core';
import { DarkModeService } from './darkmode.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  darkMode:boolean;
  fsMode:boolean;
  timer: boolean;

  constructor(private dm: DarkModeService) { }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
    this.dm.currentTimer.subscribe(timer => this.timer = timer);
    this.dm.currentFSmode.subscribe(fsMode => this.fsMode = fsMode);

    const body = document.getElementsByTagName('mat-card')[0];
    const nav = document.getElementsByTagName('mat-toolbar')[0];
    if(this.darkMode) {
      const slider: HTMLInputElement = <HTMLInputElement>document.getElementsByClassName('dmtoggle')[0];
      slider.checked = true;
      body.classList.add('darkMode');
      nav.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
      nav.classList.remove('darkMode');
    }
    if(this.timer) {
      const slider: HTMLInputElement = <HTMLInputElement>document.getElementsByClassName('timerToggle')[0];
      slider.checked = true;
    }

    if(this.fsMode && window.innerHeight == screen.height) {
      const slider: HTMLInputElement = <HTMLInputElement>document.getElementsByClassName('FStoggle')[0];
      slider.checked = true;
    }
    else{
      const slider: HTMLInputElement = <HTMLInputElement>document.getElementsByClassName('FStoggle')[0];
      slider.checked = false;
    }
  }

  toggleDarkMode() {
    this.dm.toggleDarkMode(!this.darkMode);
    const body = document.getElementsByTagName('mat-card')[0];
    const nav = document.getElementsByTagName('mat-toolbar')[0];
    if(this.darkMode) {
      body.classList.add('darkMode');
      nav.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
      nav.classList.remove('darkMode');
    }
  }

  toggleFullScreen() {
    this.dm.toggleFSmode(!this.fsMode);
    if(window.innerHeight != screen.height) { //if not already in fullscreen mode
      let elem = document.body;
      let methodToBeInvoked = elem.requestFullscreen ||
        elem.webkitRequestFullscreen || elem['mozRequestFullscreen'] ||
        elem['msRequestFullscreen'];
        if(methodToBeInvoked) methodToBeInvoked.call(elem);
    }
    else { //else exit fullscreen, elem.exitFullscreen not allowed in TS???
    }
  }

  toggleTimer() {
   this.dm.toggleTimer(!this.timer);
  }

}
