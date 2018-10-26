import { Component, OnInit } from '@angular/core';

import { DarkModeService } from '../settings/darkmode.service';
import { AuthService } from '../auth.service';
import { WebService } from '../web.service';
import { Subscription } from 'rxjs';


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

  onRefresh(){
    this.stomp.setConnected();
    this.stomp.getStats();
    //subscribe to stats response
    this.sub = this.stomp.stompClient.subscribe('/topic/getStats', (res) => {
      let r = JSON.parse(res.body) as StatsResponse;
      console.log(r);
      this.stats = r.statsList;
    }); 
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

