import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';


// Angular Material Modules
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { NavigationComponent } from './navigation/navigation.component';
import { MainMenuComponent } from './main-menu/main-menu.component';
import { GridComponent } from './grid/grid.component';
import { CellComponent } from './cell/cell.component';
import { GameControlsComponent } from './game-controls/game-controls.component';
import { NewGameMenuComponent } from './new-game-menu/new-game-menu.component';
import { SettingsComponent } from './settings/settings.component';
import { CreateStrategyComponent } from './create-strategy/create-strategy.component';
import { StatisticsComponent } from './statistics/statistics.component';
import { AboutComponent } from './about/about.component';

@NgModule({
  declarations: [
    AppComponent, 
    NavigationComponent, MainMenuComponent, GridComponent, CellComponent, GameControlsComponent, NewGameMenuComponent, SettingsComponent, CreateStrategyComponent, StatisticsComponent, AboutComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatCardModule,
    MatButtonModule,
    RouterModule.forRoot([
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: MainMenuComponent } ,
      { path: 'newGame', component: NewGameMenuComponent } ,
      { path: 'settings', component: SettingsComponent } ,
      { path: 'statistics', component: StatisticsComponent },
      { path: 'createStrategy', component: CreateStrategyComponent },
      { path: 'about', component: AboutComponent }
    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
