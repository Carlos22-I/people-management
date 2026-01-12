import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { PersonService } from '../../../api/person.service';

@Component({
	selector: 'app-person-insert',
	imports: [
		ReactiveFormsModule
	],
	templateUrl: './person-insert.html',
	styleUrl: './person-insert.css',
	providers: []
})

export class PersonInsert {
	formPerson: FormGroup;

	get firstNameFb() { return this.formPerson.controls['firstName']; }
	get surNameFb() { return this.formPerson.controls['surName']; }
	get dniFb() { return this.formPerson.controls['dni']; }
	get genderFb() { return this.formPerson.controls['gender']; }
	get birthDateFb() { return this.formPerson.controls['birthDate']; }

	constructor(
		private formBuilder: FormBuilder,
		private personService: PersonService
	) {
		this.formPerson = this.formBuilder.group({
			'firstName': [null, []],
			'surName': [null, []],
			'dni': [null, []],
			'gender': [null, []],
			'birthDate': [null, []],
		});
	}

	public insert(): void {
		let formData = new FormData();

		formData.append('dto.person.firstName', this.firstNameFb.value);
		formData.append('dto.person.surName', this.surNameFb.value);
		formData.append('dto.person.dni', this.dniFb.value);
		formData.append('dto.person.gender', this.genderFb.value);
		formData.append('dto.person.birthDate', this.birthDateFb.value);

		this.personService.insert(formData).subscribe({
			next: (response: any) => {
				console.log(response);
			},
			error: (error: any) => {
				console.log(error);
			}
		});
	}
}