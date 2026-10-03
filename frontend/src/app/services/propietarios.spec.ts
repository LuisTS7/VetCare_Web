import { TestBed } from '@angular/core/testing';
import { Propietarios } from './propietarios';

describe('Propietarios', () => {
  let service: Propietarios;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Propietarios);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
