import { Component, OnInit } from '@angular/core';

import { DarkModeService } from '../settings/darkmode.service';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  darkMode:boolean;

  constructor(private dm: DarkModeService) { }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
    const body = document.getElementsByTagName('mat-card')[0];
    if(this.darkMode) {
      body.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
    }
  }

}
