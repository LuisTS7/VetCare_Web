import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PropietarioForm } from './propietario-form';

describe('PropietarioForm', () => {
  let component: PropietarioForm;
  let fixture: ComponentFixture<PropietarioForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PropietarioForm],
    }).compileComponents();

    fixture = TestBed.createComponent(PropietarioForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
