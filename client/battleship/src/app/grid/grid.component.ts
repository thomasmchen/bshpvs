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
    window.alert(n);
  }

}
