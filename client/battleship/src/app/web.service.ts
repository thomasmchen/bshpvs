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
  public connected;

  constructor() {
    this.connected = false;
  }

  initializeConnection() {
    let socket = new SockJS(this.backendUrl);
    this.stompClient = Stomp.over(socket);
    let that = this;
    this.stompClient.connect({}, function (frame) {
      that.stompClient.subscribe('/topic/confirmPlacement', (res) => {
        console.log('something happend');
      });
    });
  }
  
  setConnected() {
    this.connected = true;
  }

  isConnected() {
    return this.connected;
  }

  showGreeting(message) {
    console.log("Message recieved from backend " + message);
  }

  sendMessage(message) {
    this.stompClient.send(this.messageUrl, {}, message);
  }
}
