import { Component, OnInit } from '@angular/core';

import { DarkModeService } from '../settings/darkmode.service';

@Component({
  selector: 'app-about',
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
export class AboutComponent implements OnInit {

  darkMode:boolean;

  constructor(private dm: DarkModeService) { }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
  }

}
