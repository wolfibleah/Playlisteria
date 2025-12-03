import { Component } from '@angular/core';

@Component({
  selector: 'app-userprofile',
  templateUrl: './userprofile.component.html',
  styleUrls: ['./userprofile.component.scss'],
})
export class UserprofileComponent {
  isEditing = false;
  toggleEditing(): void {
    this.isEditing = !this.isEditing;
  }
}
