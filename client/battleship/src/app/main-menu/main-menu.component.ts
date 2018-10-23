import { Component, OnInit, NgZone} from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';

import { DarkModeService } from '../settings/darkmode.service';
declare var gapi : any;
@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.css']

})

export class MainMenuComponent implements OnInit {

  darkMode:boolean;


  constructor(private router: Router, private dm: DarkModeService, private auth: AuthService) {}

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
    this.router.navigateByUrl('/statistics');

  }

  createStrategyClicked() {
    this.router.navigateByUrl('/createStrategy');
  }

  aboutClicked() {
    this.router.navigateByUrl('/about');
  }
  logOutClicked() {
    //declare var gapi : any;
    var auth2 = gapi.auth2.getAuthInstance();
    auth2.signOut().then(function () {
      console.log('User signed out.');
    });


    this.router.navigateByUrl('/login');
    //onSignIn(auth2);
  }


}
