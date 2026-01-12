import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-advertising-section',
	imports: [],
	templateUrl: './advertising-section.html',
	styleUrl: './advertising-section.css',
})

export class AdvertisingSection {
	@Input() inputFirstName: string = '';

	@Output() outputSuscription: any = new EventEmitter<any>();

	saveSuscribe(param: string): void {
		let sendObject: any = {
			fullName: this.inputFirstName,
			suscriptionType: param
		}

		this.outputSuscription.emit(sendObject);
	}
}