import { Component, inject } from '@angular/core';
import { RouterOutlet, RouterLink, Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
	selector: 'app-root',
	standalone: true,
	imports: [RouterOutlet, RouterLink],
	templateUrl: './app.html',
	styleUrl: './app.css'
})

export class App {
	isLoggedIn = false;
	private router = inject(Router);

	constructor() {
		this.router.events.pipe(
			filter(event => event instanceof NavigationEnd)
		).subscribe(() => {
			this.checkLogin();
		});
	}

	checkLogin() {
		this.isLoggedIn = !!localStorage.getItem('token');
	}

	logout() {
		localStorage.removeItem('token');
		this.isLoggedIn = false;
		this.router.navigate(['/']);
	}
}
