import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'dateFormat'
})

export class DateFormatPipe implements PipeTransform {
	transform(value: string, ...args: string[]): string {
		let dateConverted = null;
		let arrayDateTemp = value.split('-');

		if(args[0] == 'fullDate') {
			let timeDayPart = arrayDateTemp[2].split('T');
			let dayPart = timeDayPart[0];
			let timePart = timeDayPart[1].split(':');

			let hourPart = timePart[0];
			let minutesPart = timePart[1];

			dateConverted = `${dayPart}/${arrayDateTemp[1]}/${arrayDateTemp[0]} ${hourPart}:${minutesPart}`;
		} else {
			dateConverted = `${arrayDateTemp[2]}/${arrayDateTemp[1]}/${arrayDateTemp[0]}`;
		}

		return dateConverted;
	}
}