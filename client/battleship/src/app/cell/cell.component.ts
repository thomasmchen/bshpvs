import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-cell',
  templateUrl: './cell.component.html',
  styleUrls: ['./cell.component.css']
})
export class CellComponent implements OnInit {

  @Input()
  width;

  @Input()
  height;
  
  constructor() { }

  ngOnInit() {
  }

  /*styleObject(): Object {
    if (true){
        return {'height': this.height + 'px',
        'width': this.width + "px",
        'background-color': '#90ee90'}
    }
  }*/
}


