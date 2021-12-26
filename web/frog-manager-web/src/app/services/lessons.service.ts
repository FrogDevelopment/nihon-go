import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {InputDto} from '../pages/lessons/entities/InputDto';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LessonsService {

  private readonly serviceUrl: string;

  constructor(private http: HttpClient) {
    this.serviceUrl = `${environment.baseUrl}/lessons`;
  }

  getDtos(lesson: number, sortField: string, sortOrder: string): Observable<InputDto[]> {
    let params = new HttpParams()
      .set('lesson', `${lesson}`)
    ;

    if (sortField) {
      params = params.set('sortField', `${sortField}`);
    }
    if (sortOrder) {
      params = params.set('sortOrder', `${sortOrder}`);
    }

    return this.http.get<InputDto[]>(this.serviceUrl, {params});
  }

  insert(dto: InputDto): Observable<InputDto> {
    return this.http.post<InputDto>(this.serviceUrl, dto);
  }

  update(dto: InputDto): Observable<InputDto> {
    return this.http.put<InputDto>(this.serviceUrl, dto);
  }

  delete(dto: InputDto): Observable<any> {
    return this.http.request('delete', this.serviceUrl, {body: dto});
  }
}
