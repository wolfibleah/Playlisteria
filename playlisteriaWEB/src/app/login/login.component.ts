import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import * as CryptoJS from 'crypto-js';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
    ]),
  });
  errorMessage: string = '';

  constructor(
    private readonly router: Router,
    private readonly userService: UserService,
  ) {}

  ngOnInit(): void {}

  login(): void {
    if (this.loginForm.valid) {
      const user: User = {
        username: this.loginForm.value.username,
        password: CryptoJS.SHA256(this.loginForm.value.password).toString(),
      };

      this.userService.login(user).subscribe({
        next: (res) => {
          console.log('Login: OK!');
          console.log(res);
          this.router.navigate(['navigate']);
        },
        error: (err) => {
          console.error('Eroare la login:', err.message);
          this.errorMessage = 'Invalid username or password. Please try again!';
        },
      });

      console.log('S-a logat utilizatorul:', user);
    } else {
      console.log('Parola sau username gresite!');
      this.errorMessage = 'Invalid username or password. Please try again!';
    }
  }
}
