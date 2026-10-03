import { TestBed } from '@angular/core/testing';
import { Atenciones } from './atenciones';

describe('Atenciones', () => {
  let service: Atenciones;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Atenciones);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
