import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateStrategyComponent } from './create-strategy.component';

describe('CreateStrategyComponent', () => {
  let component: CreateStrategyComponent;
  let fixture: ComponentFixture<CreateStrategyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateStrategyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateStrategyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
