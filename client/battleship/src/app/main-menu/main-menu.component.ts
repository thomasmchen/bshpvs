import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-menu',
  templateUrl: './main-menu.component.html',
  styleUrls: ['./main-menu.component.css']
})
export class MainMenuComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
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

}
