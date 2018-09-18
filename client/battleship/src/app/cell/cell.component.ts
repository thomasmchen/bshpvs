import { Component, OnInit, Input } from '@angular/core';
import { NgModuleCompileResult } from '@angular/compiler/src/ng_module_compiler';

@Component({
  selector: 'app-cell',
  templateUrl: './cell.component.html',
  styleUrls: ['./cell.component.css']
})
export class CellComponent implements OnInit {

  @Input()
  n;

  @Input()
  numcols;
  
  constructor() { }

  ngOnInit() {
  }

  onClick() {
    // convert inputs to numbers
    var index = +this.n;
    var ncols = +this.numcols;

    // calculate the row and col
    var row = Math.floor(index / (+ncols));
    var col = index % (+ncols);

    //report what cell has been selected.
    window.alert("Row: " + row + " Col: " + col);

  }
  /*styleObject(): Object {
    if (true){
        return {'height': this.height + 'px',
        'width': this.width + "px",
        'background-color': '#90ee90'}
    }
  }*/
}


