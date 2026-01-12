// person.service.ts
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})

export class Person {
	constructor(
		private httpClient: HttpClient
	) {}

	public insert(personData: any): Observable<any> {
		// 1. Obtener el token del localStorage
		const token = localStorage.getItem('token');
		
		// 2. Crear headers con el token
		let headers = new HttpHeaders({
			'Content-Type': 'application/json'
		});
		
		if (token) {
			headers = headers.set('Authorization', `Bearer ${token}`);
		}
		
		console.log('Token enviado:', token); // Para depurar
		
		// 3. Enviar como JSON
		return this.httpClient.post('http://localhost:8080/person/insert', personData, { 
			headers: headers 
		});
	}

	public getAll(): Observable<any> {
		const token = localStorage.getItem('token');
		let headers = new HttpHeaders();
		
		if (token) {
			headers = headers.set('Authorization', `Bearer ${token}`);
		}
		
		return this.httpClient.get('http://localhost:8080/person/getall', { 
			headers: headers 
		});
	}
}