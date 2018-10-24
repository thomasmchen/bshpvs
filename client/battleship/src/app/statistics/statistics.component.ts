import { Component, OnInit } from '@angular/core';

import { DarkModeService } from '../settings/darkmode.service';
import { AuthService } from '../auth.service';
import { WebService } from '../web.service';
import { Subscription } from 'rxjs';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  /*
  {position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H'},
  {position: 2, name: 'Helium', weight: 4.0026, symbol: 'He'},
  {position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li'},
  {position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be'},
  {position: 5, name: 'Boron', weight: 10.811, symbol: 'B'},
  {position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C'},
  {position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N'},
  {position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O'},
  {position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F'},
  {position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne'},
  */
];


@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {
  displayedColumns: string[] = ['numPlayers', 'hitPerc', 'missPerc', 'numTurns', 'time', 'winner', 'playerTypes'];

  darkMode:boolean;
  user_id:string;
  stats:DBStat[];
  sub:Subscription;

  constructor(private dm: DarkModeService, private stomp: WebService, private auth: AuthService) { 
    this.stomp.getStats();
    //subscribe to stats response
    this.sub = this.stomp.stompClient.subscribe('/topic/getStats', (res) => {
      let r = JSON.parse(res.body) as StatsResponse;
      console.log(r);
      this.stats = r.statsList;
    });     
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

  onRefresh(){
    this.stomp.setConnected();
    this.stomp.getStats();
    //subscribe to stats response
    this.sub = this.stomp.stompClient.subscribe('/topic/getStats', (res) => {
      let r = JSON.parse(res.body) as StatsResponse;
      console.log(r);
      this.stats = r.statsList;
    }); 
/*
    //for testing if data was received 
    var count = 0;
    while(this.stats[count] != null){
      alert(this.stats[count].numPlayer);
      count++;
    }
  */
  }

}


interface DBStat{
  hitPerc: number,
  missPerc: number,
  numTurns: number,
  time: number,
  winner: String,
  playerTypes: String,
  numPlayer: number
}

interface StatsResponse{
  statsList: DBStat[],
}

