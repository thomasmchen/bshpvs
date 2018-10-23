import { Component, OnInit } from '@angular/core';

import { DarkModeService } from '../settings/darkmode.service';
import { AuthService } from '../auth.service';
import { WebService } from '../web.service';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent implements OnInit {

  darkMode:boolean;
  user_id:string;
  stats:DBStat[];

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
    this.stomp.stompClient.subscribe('/topic/getStats', (res) => {
      let r = JSON.parse(res.body) as StatsResponse;
      console.log(r);
      this.stats = r.statsList;
    }); 

    var count = 0;
    while(this.stats[count+1] != null){
      alert(this.stats[count].numTurns);
      count++;
    }
  }

}


interface DBStat{
  numPlayers: number,
  hitPerc: number,
  missPerc: number,
  numTurns: number,
  time: number,
  winner: String,
  playerTypes: String
}

interface StatsResponse{
  statsList: DBStat[]
}

