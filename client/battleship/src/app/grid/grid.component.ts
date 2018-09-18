import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent implements OnInit {

  // input to get number of columns
  numCols: number;

  // representation of every column
  cols: string[];

  constructor() {
    this.numCols = 15;
    this.cols = new Array((this.numCols) * 15).fill('');
  }

  ngOnInit() {
  }

}
