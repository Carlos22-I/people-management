import { Component } from '@angular/core';
import { PersonService } from '../../../api/person.service';
import { CommonModule } from '@angular/common';
import { DateFormatPipe } from '../../../pipe/date-format-pipe';

@Component({
	selector: 'app-person-get-all',
	imports: [
		CommonModule,
		DateFormatPipe
	],
	templateUrl: './person-get-all.html',
	styleUrl: './person-get-all.css'
})

export class PersonGetAll {
	listPerson: any[] = [];

	constructor(
		private personService: PersonService
	){}

	ngOnInit(): void {
		this.personService.getAll().subscribe({
			next: (response: any) => {
				this.listPerson = response;
			},
			error: (error: any) => {
				console.log(error);
			}
		});
	}
}