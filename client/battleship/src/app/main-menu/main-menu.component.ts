import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { DarkModeService } from '../settings/darkmode.service';
import { AuthService } from '../auth.service';
import { WebService } from '../web.service';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.css']
})
export class MainMenuComponent implements OnInit {

  darkMode:boolean;
  user_id:String;

  constructor(private router: Router, private dm: DarkModeService, private auth: AuthService, private stomp: WebService) { 
    stomp.initializeConnection();

  }

  ngOnInit() {
    this.dm.currentDarkMode.subscribe(darkMode => this.darkMode = darkMode);
    const body = document.getElementsByTagName('mat-card')[0];
    if(this.darkMode) {
      body.classList.add('darkMode');
    } else {
      body.classList.remove('darkMode');
    }
  }

  newGameClicked() {
    this.router.navigateByUrl('/newGame');
  }

  settingsClicked() {
    this.router.navigateByUrl('/settings');
  }

  statisticsClicked() {
    this.stomp.setConnected();
    this.auth.currentid.subscribe(user_id => this.user_id = user_id);
    let sendFormat = "{\"id\":\""+this.user_id+"\"}";
    this.stomp.sendID(sendFormat);

    this.router.navigateByUrl('/statistics');

  }

  createStrategyClicked() {
    this.router.navigateByUrl('/createStrategy');
  }

  aboutClicked() {
    this.router.navigateByUrl('/about');
  }

}
