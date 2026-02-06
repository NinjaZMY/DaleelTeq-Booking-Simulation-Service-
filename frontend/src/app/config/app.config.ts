import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AppConfig {
  // API Base URL - will proxy to backend during dev, direct in production
  public API_BASE = '/api';

  // Time window configuration
  public TIME_WINDOW = {
    start: '09:00',
    end: '16:00'
  };

  // Service time values (in minutes)
  public SERVICE_DURATIONS = [15, 20, 25, 30];

  constructor() { }
}
