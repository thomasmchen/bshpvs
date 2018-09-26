import { Component, OnInit } from '@angular/core';
import { DarkModeService } from './darkmode.service';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {

  darkMode:boolean;
  

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

}
