import { Component, OnInit, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../auth.service';
import { WebService } from '../web.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router, zone: NgZone, private auth: AuthService, private stomp: WebService) { 
    window['onSignIn'] = (user) => zone.run(() => this.onSignIn(user));
  }

  ngOnInit() {
  }

  onSignIn(googleUser) {
    if (this.auth.signIn(googleUser)) {
      this.router.navigateByUrl('/home');
    }

    this.stomp.setConnected();
    var userID = googleUser.getUserID();
    let sendFormat = "{\"id\":\""+userID+"\"}";
    this.stomp.sendID(sendFormat);

  }

}
