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
}