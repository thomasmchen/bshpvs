import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService implements CanActivate {
  name: string = "";
  email: string = "";
  signedIn: boolean = false;

  constructor(private router: Router) { }

  signIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.

    this.email = profile.getEmail();
    this.name = profile.getName();
    this.signedIn = true;
    return true;
  }

  canActivate() {
    if (!this.signedIn) {
      this.router.navigateByUrl('/login');
    }
    return this.signedIn;
  }
}
