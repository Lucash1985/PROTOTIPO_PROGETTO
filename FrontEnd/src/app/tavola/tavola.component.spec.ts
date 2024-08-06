import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TavolaComponent } from './tavola.component';

describe('TavolaComponent', () => {
  let component: TavolaComponent;
  let fixture: ComponentFixture<TavolaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TavolaComponent]
    });
    fixture = TestBed.createComponent(TavolaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
