import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  userName = '';
  password = '';

  constructor(private auth: Auth, private router: Router) {}

  login() {
    const data = { userName: this.userName, password: this.password };

    this.auth.login(data).subscribe(res => {
      this.auth.saveToken(res.token);
      this.router.navigate(['/home']);
    });
  }
}
