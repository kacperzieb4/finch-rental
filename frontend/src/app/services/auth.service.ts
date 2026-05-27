import { Injectable, signal } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly AUTH_KEY = 'finch_rental_auth';
  
  isLoggedIn = signal<boolean>(this.checkLoginStatus());

  private checkLoginStatus(): boolean {
    return localStorage.getItem(this.AUTH_KEY) !== null;
  }

  login(username: string, password: string): boolean {
    if (username === 'admin' && password === 'admin123') {
      const credentials = btoa(`${username}:${password}`);
      localStorage.setItem(this.AUTH_KEY, credentials);
      this.isLoggedIn.set(true);
      return true;
    }
    return false;
  }

  logout(): void {
    localStorage.removeItem(this.AUTH_KEY);
    this.isLoggedIn.set(false);
  }

  getAuthHeaders(): HttpHeaders {
    const credentials = localStorage.getItem(this.AUTH_KEY);
    if (credentials) {
      return new HttpHeaders({
        'Authorization': `Basic ${credentials}`
      });
    }
    return new HttpHeaders();
  }
}
