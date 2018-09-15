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

@NgModule({
  declarations: [
    AppComponent, 
    NavigationComponent, MainMenuComponent, GridComponent, CellComponent, GameControlsComponent
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
    ])
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
