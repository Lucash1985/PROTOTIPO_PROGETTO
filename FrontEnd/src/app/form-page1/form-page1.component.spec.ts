import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormPage1Component } from './form-page1.component';

describe('FormPage1Component', () => {
  let component: FormPage1Component;
  let fixture: ComponentFixture<FormPage1Component>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FormPage1Component]
    });
    fixture = TestBed.createComponent(FormPage1Component);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
