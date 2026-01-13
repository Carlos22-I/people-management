import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgFor, NgIf, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PersonService } from '../../api/person.service';

@Component({
    selector: 'app-home',
    standalone: true,
    imports: [NgFor, NgIf, FormsModule, DatePipe],
    templateUrl: './home.html',
    styleUrl: './home.css'
})
export class Home implements OnInit {

    persons: any[] = [];
    originalPersons: any[] = [];
    total: number = 0;
    searchTerm: string = '';

    showModal = false;
    modalMode: 'create' | 'edit' = 'create';
    selectedPerson: any = {
        idPerson: '', firstName: '', surName: '', dni: '', gender: true, birthDate: ''
    };

    constructor(
        private personService: PersonService,
        private router: Router
    ) { }

    ngOnInit() {
        this.loadData();
    }

    loadData() {
        this.personService.getAll().subscribe((res: any[]) => {
            this.originalPersons = res;
            this.filter();
        });
        this.personService.count().subscribe((c: number) => {
            this.total = c;
        });
    }

    filter() {
        if (!this.searchTerm) {
            // Si no hay término, recagar todos (o restaurar originales si ya los tenemos)
            this.personService.getAll().subscribe(res => {
                this.persons = res;
                this.originalPersons = res;
            });
        } else {
            // Búsqueda por nombre (ejemplo básico, se podría combinar con apellido)
            this.personService.searchByName(this.searchTerm).subscribe(res => {
                this.persons = res;
                if (res.length === 0) {
                    // Si no encuentra por nombre, intentar por apellido
                    this.personService.searchBySurname(this.searchTerm).subscribe(resSurname => {
                        this.persons = resSurname;
                    });
                }
            });
        }
    }

    deletePerson(id: string) {
        if (confirm('¿Estás seguro de eliminar este registro?')) {
            this.personService.delete(id).subscribe(() => {
                this.loadData();
            });
        }
    }

    openModal(mode: 'create' | 'edit', person?: any) {
        this.modalMode = mode;
        this.showModal = true;
        if (mode === 'edit' && person) {
            this.selectedPerson = { ...person };
            if (this.selectedPerson.birthDate) {
                this.selectedPerson.birthDate = new Date(this.selectedPerson.birthDate).toISOString().split('T')[0];
            }
        } else {
            this.selectedPerson = { idPerson: '', firstName: '', surName: '', dni: '', gender: true, birthDate: '' };
        }
    }

    closeModal() {
        this.showModal = false;
    }

    savePerson() {
        this.personService.insert(this.selectedPerson).subscribe(() => {
            this.closeModal();
            this.loadData();
        });
    }

    download(file: Blob, filename: string) {
        const url = window.URL.createObjectURL(file);
        const a = document.createElement('a');
        a.href = url;
        a.download = filename;
        a.click();
        window.URL.revokeObjectURL(url);
    }

    exportTxt() {
        this.personService.exportTxt(this.searchTerm).subscribe(res => this.download(res, 'personas.txt'));
    }

    exportPdf() {
        this.personService.exportPdf(this.searchTerm).subscribe(res => this.download(res, 'personas.pdf'));
    }

    exportXlsx() {
        this.personService.exportXlsx(this.searchTerm).subscribe(res => this.download(res, 'personas.xlsx'));
    }
}
