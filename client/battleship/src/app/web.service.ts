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
  private windowInitUrl = '/app/windowInit';
  private turnUrl = '/app/turn';
  private checkWinUrl = '/app/checkWin';
  public stompClient = null;
  public connected = false;

  constructor() { }

  initializeConnection() {
    let socket = new SockJS(this.backendUrl);
    this.stompClient = Stomp.over(socket);
    let that = this;
    this.stompClient.connect({}, function (frame) {
        that.setConnected();
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

  sendGameWindowInit() {
    this.stompClient.send(this.windowInitUrl, {}, "window");
  }

  sendMove(attack) {
    this.stompClient.send(this.turnUrl, {}, attack);
  }

  checkWin() {
    this.stompClient.send(this.checkWinUrl, {}, "");
  }
}
