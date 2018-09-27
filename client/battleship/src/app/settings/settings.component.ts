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

  constructor(private dm: DarkModeService) { }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
    const body = document.getElementsByTagName('mat-card')[0];
    if(this.darkMode) {
      const slider: HTMLInputElement = <HTMLInputElement>document.getElementsByClassName('dmtoggle')[0];
      slider.checked = true;
      body.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
    }

    if(window.innerHeight != screen.height){
      const slider: HTMLInputElement = <HTMLInputElement>document.getElementsByClassName('dmtoggle')[0];
      slider.checked = false;
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

  @ViewChild('input') private input;

  toggleFullScreen() {
    if(window.innerHeight != screen.height) { //if not already in fullscreen mode
      let elem = document.body;
      let methodToBeInvoked = elem.requestFullscreen ||
        elem.webkitRequestFullscreen || elem['mozRequestFullscreen'] ||
        elem['msRequestFullscreen'];
        if(methodToBeInvoked) methodToBeInvoked.call(elem);
    }
    else { //else exit fullscreen
      this.input.nativeElement.focus();
      let startPos = this.input.nativeElement.selectionStart;
      let value = this.input.nativeElement.value;
      this.input.nativeElement.value=
      value.substring(0, startPos) + 'YEEEEEET ' + value.substring(startPos, value.length)
    }
  }

}
