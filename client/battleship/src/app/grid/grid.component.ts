import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent implements OnInit {

  // input to get number of columns
  @Input()
  numcols : string;

  @Output()
  cellClicked = new EventEmitter<any>();

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
   this.cellClicked.emit({
    row: event.row,
    col: event.col
  });
  }

}

interface cell {
  row: number,
  col: number
}
