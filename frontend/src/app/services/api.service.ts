import { Injectable } from '@angular/core';
import { AppConfig } from '../config/app.config';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private config: AppConfig) { }

  private handleResponse(response: Response) {
    if (!response.ok) {
      throw new Error(`HTTP Error: ${response.status} ${response.statusText}`);
    }
    return response.json();
  }

  // Generic GET
  get(endpoint: string) {
    return fetch(`${this.config.API_BASE}${endpoint}`)
      .then(r => this.handleResponse(r))
      .catch(e => Promise.reject({ error: e.message }));
  }

  // Generic POST
  post(endpoint: string, body: any) {
    return fetch(`${this.config.API_BASE}${endpoint}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    })
      .then(r => r.json())
      .catch(e => Promise.reject({ error: e.message }));
  }

  // Generic PUT
  put(endpoint: string, body: any) {
    return fetch(`${this.config.API_BASE}${endpoint}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    })
      .then(r => r.json())
      .catch(e => Promise.reject({ error: e.message }));
  }

  // Generic DELETE
  delete(endpoint: string) {
    return fetch(`${this.config.API_BASE}${endpoint}`, {
      method: 'DELETE',
      headers: { 'Content-Type': 'application/json' }
    })
      .then(r => {
        if (r.status === 204) return { status: 204 };
        return r.json();
      })
      .catch(e => Promise.reject({ error: e.message }));
  }

  // Generic PATCH
  patch(endpoint: string, body: any) {
    return fetch(`${this.config.API_BASE}${endpoint}`, {
      method: 'PATCH',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(body)
    })
      .then(r => r.json())
      .catch(e => Promise.reject({ error: e.message }));
  }
}
