import { Injectable } from '@angular/core';
import { Component } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root'
})
export class WebService {

  private backendUrl = 'http://localhost:8080/battleship';
  private messageUrl = '/app/placeShips';
  public stompClient = null;
  connected = false;

  constructor() { 

  }



  initializeConnection() {
    let socket = new SockJS(this.backendUrl);
    this.stompClient = Stomp.over(socket);
    let that = this;
    this.stompClient.connect({}, function (frame) {
      that.stompClient.subscribe('/topic/getUserShips', (res) => {
        console.log('something happend');
      });
    });
  }
  

  isConnected() {
    return this.connected;
  }

  showGreeting(message) {
    console.log("Message recieved from backend " + message);
  }

  sendMessage(message) {
    this.stompClient.send(this.messageUrl, {}, JSON.stringify({'name': 'money'}));
  }
}
