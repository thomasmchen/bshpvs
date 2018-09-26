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
  }

  toggleDarkMode() {
    this.dm.toggleDarkMode(!this.darkMode);
  }

}
