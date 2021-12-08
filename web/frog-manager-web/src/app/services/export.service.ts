import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ExportService {

  private readonly serviceUrl: string;

  constructor(private http: HttpClient) {
    this.serviceUrl = `${environment.baseUrl}/export`;
  }

  getInfo(lesson: number): Observable<LessonInfo> {
    return this.http.get<LessonInfo>(`${this.serviceUrl}/${lesson}`);
  }

  export(lesson: number): Observable<any> {
    return this.http.post(`${this.serviceUrl}/${lesson}`, null);
  }
}

export class LessonInfo {
  updateDateTime: string;
  exportDateTime: string;
}
