import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {InputDto} from '../pages/lessons/entities/InputDto';
import {environment} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LessonsService {

  constructor(private http: HttpClient) {
  }

  getTotal(): Observable<number> {
    return this.http.get<number>(`${environment.baseUrl}/nihongo/lessons/total`);
  }

  getTags(): Observable<Array<string>> {
    return this.http.get<Array<string>>(`${environment.baseUrl}/nihongo/lessons/tags`);
  }

  getDtos(pageIndex: number = 1, pageSize: number = 10, sortField: string, sortOrder: string): Observable<InputDto[]> {
    let params = new HttpParams()
      .set('pageIndex', `${pageIndex}`)
      .set('pageSize', `${pageSize}`)
    ;

    if (sortField) {
      params = params.set('sortField', `${sortField}`);
    }
    if (sortOrder) {
      params = params.set('sortOrder', `${sortOrder}`);
    }

    return this.http.get<InputDto[]>(`${environment.baseUrl}/nihongo/lessons/fetch`, {params});
  }

  insert(dto: InputDto): Observable<InputDto> {
    return this.http.post<InputDto>(`${environment.baseUrl}/nihongo/lessons/admin`, dto);
  }

  update(dto: InputDto): Observable<InputDto> {
    return this.http.put<InputDto>(`${environment.baseUrl}/nihongo/lessons/admin`, dto);
  }

  delete(dto: InputDto): Observable<any> {
    return this.http.request('delete', `${environment.baseUrl}/nihongo/lessons/admin`, {body: dto});
  }
}
