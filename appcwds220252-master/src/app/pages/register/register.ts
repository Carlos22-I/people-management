import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PersonService } from '../../api/person.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {

  person = {
    firstName: '',
    surName: '',
    dni: '',
    gender: '', 
    birthDate: ''
  };

  constructor(private personService: PersonService, private router: Router) {}

  register() {
    // Convertir gender de string a boolean
    // true = 'M' o '1', false = cualquier otra cosa
    const genderBoolean = this.person.gender === 'M' || this.person.gender === '1';
    
    const requestData = {
      dto: {
        person: {
          firstName: this.person.firstName,
          surName: this.person.surName,
          dni: this.person.dni,
          gender: genderBoolean,  // Enviar como boolean
          birthDate: this.person.birthDate
        }
      }
    };

    console.log('Datos a enviar:', requestData);
    
    this.personService.insert(requestData).subscribe({
      next: (response) => {
        console.log('Éxito:', response);
        this.router.navigate(['/home']);
      },
      error: (error) => {
        console.error('Error:', error);
        alert('Error: ' + (error.error?.message || error.statusText));
      }
    });
  }
 
}