import { BrowserModule } from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { AuthService } from './auth.service';



// Angular Material Modules
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatSnackBarModule } from '@angular/material/snack-bar';

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
import { GameWindowComponent } from './game-window/game-window.component';
import { LoginComponent } from './login/login.component';

@NgModule({
  declarations: [
    AppComponent, 
    NavigationComponent, MainMenuComponent, GridComponent, CellComponent, GameControlsComponent, NewGameMenuComponent, SettingsComponent, CreateStrategyComponent, StatisticsComponent, AboutComponent, GameWindowComponent, LoginComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    MatToolbarModule,
    MatCardModule,
    MatSnackBarModule,
    MatButtonModule,
    MatGridListModule,
    RouterModule.forRoot([
      { path: '', redirectTo: 'login', pathMatch: 'full' },
      { path: 'home', component: MainMenuComponent, canActivate: [AuthService]} ,
      { path: 'newGame', component: NewGameMenuComponent, canActivate: [AuthService] } ,
      { path: 'settings', component: SettingsComponent, canActivate: [AuthService] } ,
      { path: 'statistics', component: StatisticsComponent, canActivate: [AuthService] },
      { path: 'createStrategy', component: CreateStrategyComponent, canActivate: [AuthService] },
      { path: 'about', component: AboutComponent, canActivate: [AuthService] },
      { path: 'grid', component: GridComponent, canActivate: [AuthService] },
      { path: 'gameWindow', component: GameWindowComponent, canActivate: [AuthService] },
      { path: 'newGame', component: NewGameMenuComponent, canActivate: [AuthService] },
      { path: 'login', component: LoginComponent },
      { path: '**', component: LoginComponent }

    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
