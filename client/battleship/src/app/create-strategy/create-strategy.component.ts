import { Component, OnInit } from '@angular/core';

import { DarkModeService } from '../settings/darkmode.service';

@Component({
  selector: 'app-create-strategy',
  templateUrl: './create-strategy.component.html',
  styleUrls: ['./create-strategy.component.css']
})
export class CreateStrategyComponent implements OnInit {

  darkMode:boolean;

  constructor(private dm: DarkModeService) { }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
  }

}
