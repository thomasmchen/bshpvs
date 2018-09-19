import { Component, OnInit, Input } from '@angular/core';
import { WebdriverWebElement } from 'protractor/built/element';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent implements OnInit {

  // input to get number of columns
  @Input()
  numcols : string;

  // representation of every column
  cols: string[];

  constructor() {
  }

  ngOnInit() {
    var n = +this.numcols;
    this.cols = new Array(n * n).fill('');
    for (var i = 0; i < this.cols.length; i++) {
      this.cols[i] = ""+i;
    }
  }

  cellUpdate(event: cell) {
   window.alert('FROM PARENT: Row: '+ event.row + '  Col:' + event.col);
  }

}

interface cell {
  row: number,
  col: number
}
