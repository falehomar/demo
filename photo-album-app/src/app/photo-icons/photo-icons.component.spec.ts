import {ComponentFixture, TestBed} from '@angular/core/testing';

import {PhotoIconsComponent} from './photo-icons.component';

describe('PhotoIconsComponent', () => {
  let component: PhotoIconsComponent;
  let fixture: ComponentFixture<PhotoIconsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PhotoIconsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PhotoIconsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
