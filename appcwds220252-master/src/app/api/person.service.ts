// person.service.ts
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
	providedIn: 'root'
})

export class PersonService {
	constructor(
		private httpClient: HttpClient
	) { }

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

		const payload = {
			dto: {
				person: personData
			}
		};

		// 3. Enviar como JSON
		return this.httpClient.post(`${environment.apiUrl}/person/insert`, payload, {
			headers: headers
		});
	}


	public getAll(): Observable<any> {
		const token = localStorage.getItem('token');
		let headers = new HttpHeaders();

		if (token) {
			headers = headers.set('Authorization', `Bearer ${token}`);
		}

		return this.httpClient.get<any[]>(`${environment.apiUrl}/person/getall`, {
			headers: headers
		});
	}

	/*aqui se agrega */
	getById(id: string) {
		return this.httpClient.get(`${environment.apiUrl}/person/get/${id}`);
	}

	getPaged(page: number, size: number) {
		return this.httpClient.get(
			`${environment.apiUrl}/person/page?page=${page}&size=${size}`
		);
	}

	searchByName(name: string) {
		return this.httpClient.get<any[]>(
			`${environment.apiUrl}/person/search-name?value=${name}`
		);
	}

	searchBySurname(prefix: string) {
		return this.httpClient.get(
			`${environment.apiUrl}/person/search-surname?value=${prefix}`
		);
	}

	existsDni(dni: string) {
		return this.httpClient.get<boolean>(
			`${environment.apiUrl}/person/exists-dni/${dni}`
		);
	}

	count() {
		return this.httpClient.get<number>(
			`${environment.apiUrl}/person/count`
		);
	}

	delete(id: string) {
		const token = localStorage.getItem('token');
		let headers = new HttpHeaders();
		if (token) {
			headers = headers.set('Authorization', `Bearer ${token}`);
		}
		return this.httpClient.delete(
			`${environment.apiUrl}/person/delete/${id}`,
			{ headers: headers }
		);
	}
	exportTxt(search: string = '') {
		return this.httpClient.get(
			`${environment.apiUrl}/person/export/txt?search=${search}`,
			{ responseType: 'blob' }
		);
	}

	exportPdf(search: string = '') {
		return this.httpClient.get(
			`${environment.apiUrl}/person/export/pdf?search=${search}`,
			{ responseType: 'blob' }
		);
	}

	exportXlsx(search: string = '') {
		return this.httpClient.get(
			`${environment.apiUrl}/person/export/xlsx?search=${search}`,
			{ responseType: 'blob' }
		);
	}
}