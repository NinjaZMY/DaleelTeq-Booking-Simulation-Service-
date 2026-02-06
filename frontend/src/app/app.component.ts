import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'DaleelTeq Booking Simulation Service';

  ngOnInit(): void {
    console.log('✓ Angular App Initialized Successfully');
    console.log('✓ HMR Enabled - Changes will reload automatically');
  }
}
