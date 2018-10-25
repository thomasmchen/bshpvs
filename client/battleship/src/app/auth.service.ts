import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService implements CanActivate {
  name: string = "";
  email: string = "";
  signedIn: boolean = false;
  user_id:string;
  private idSource = new BehaviorSubject<string>(this.user_id);
  currentid = this.idSource.asObservable();

  constructor(private router: Router) { }

  signIn(googleUser) {
    var profile = googleUser.getBasicProfile();
    console.log('ID: ' + profile.getId()); // Do not send to your backend! Use an ID token instead.
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail()); // This is null if the 'email' scope is not present.
    this.idSource.next(profile.getId());
    this.email = profile.getEmail();
    this.name = profile.getName();
    this.signedIn = true;
    this.user_id = profile.getId();
    return true;
  }

  canActivate() {
    if (!this.signedIn) {
      this.router.navigateByUrl('/login');
    }
    return this.signedIn;
  }
}
