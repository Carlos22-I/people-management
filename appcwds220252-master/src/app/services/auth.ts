import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class Auth {

  private api = `${environment.apiUrl}/api/auth/login`;
  private http = inject(HttpClient);

  login(data: any) {
    return this.http.post<any>(this.api, data);
  }

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  getToken() {
    return localStorage.getItem('token');
  }
}
