import { Injectable } from '@angular/core'
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class DarkModeService {

    public darkMode: boolean = false;
    private darkModeSource = new BehaviorSubject<boolean>(this.darkMode);
    currentDarkMode = this.darkModeSource.asObservable();

    constructor() { }

    toggleDarkMode(darkMode: boolean) {
        this.darkModeSource.next(darkMode)
    }

    public timer: boolean = true;
    private timerSource = new BehaviorSubject<boolean>(this.timer);
    currentTimer = this.timerSource.asObservable();

    toggleTimer(timer: boolean) {
        this.timerSource.next(timer);
    }

    public fsMode: boolean = false;
    private fsModeSource = new BehaviorSubject<boolean>(this.fsMode);
    currentFSmode = this.fsModeSource.asObservable();

    toggleFSmode(fsMode: boolean) {
        this.fsModeSource.next(fsMode);
    }
}