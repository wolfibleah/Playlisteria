import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import * as CryptoJS from 'crypto-js';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup = new FormGroup({
    username: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
    ]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(4),
    ]),
    passVerif: new FormControl('', [Validators.required]),
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  constructor(
    private readonly userService: UserService,
    private readonly router: Router,
  ) {}

  ngOnInit(): void {}

  confirmPassword(): boolean {
    if (
      this.registerForm.value.password === this.registerForm.value.passVerif
    ) {
      return true;
    }
    return false;
  }

  register(): void {
    if (this.registerForm.valid && this.confirmPassword()) {
      const user: User = {
        username: this.registerForm.value.username,
        email: this.registerForm.value.email,
        password: CryptoJS.SHA256(this.registerForm.value.password).toString(),
        isAdmin: false,
      };

      this.userService.register(user).subscribe({
        next: (res) => {
          this.router.navigate(['navigate']);
        },
        error: (err) => {
          console.error('Eroare la register:', err.message);
        },
      });

      console.log('Formularul a fost trimis:', user);
    } else {
      console.log('Formularul nu a fost completat corect');
    }
  }
}
