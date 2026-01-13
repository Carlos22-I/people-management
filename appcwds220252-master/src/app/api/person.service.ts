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

	private getHeaders(): HttpHeaders {
		const token = localStorage.getItem('token');
		let headers = new HttpHeaders();
		if (token) {
			headers = headers.set('Authorization', `Bearer ${token}`);
		}
		return headers;
	}

	public insert(personData: any): Observable<any> {

		const payload = {
			dto: {
				person: personData
			}
		};

		// 3. Enviar como JSON
		// 3. Enviar como JSON
		return this.httpClient.post(`${environment.apiUrl}/person/insert`, payload, {
			headers: this.getHeaders().set('Content-Type', 'application/json')
		});
	}


	public getAll(): Observable<any> {

		return this.httpClient.get<any[]>(`${environment.apiUrl}/person/getall`, {
			headers: this.getHeaders()
		});
	}

	/*aqui se agrega */
	getById(id: string) {
		return this.httpClient.get(`${environment.apiUrl}/person/get/${id}`, { headers: this.getHeaders() });
	}

	getPaged(page: number, size: number) {
		return this.httpClient.get(
			`${environment.apiUrl}/person/page?page=${page}&size=${size}`,
			{ headers: this.getHeaders() }
		);
	}

	searchByName(name: string) {
		return this.httpClient.get<any[]>(
			`${environment.apiUrl}/person/search-name?value=${name}`,
			{ headers: this.getHeaders() }
		);
	}

	searchBySurname(prefix: string) {
		return this.httpClient.get<any[]>(
			`${environment.apiUrl}/person/search-surname?value=${prefix}`,
			{ headers: this.getHeaders() }
		);
	}

	existsDni(dni: string) {
		return this.httpClient.get<boolean>(
			`${environment.apiUrl}/person/exists-dni/${dni}`,
			{ headers: this.getHeaders() }
		);
	}

	count() {
		return this.httpClient.get<number>(
			`${environment.apiUrl}/person/count`,
			{ headers: this.getHeaders() }
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
			{ headers: this.getHeaders() }
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