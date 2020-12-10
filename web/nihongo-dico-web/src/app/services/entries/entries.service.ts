import {Injectable} from '@angular/core';

import {HttpClient} from '@angular/common/http';
import {Search} from './search';
import {SearchDetails} from '../../pages/search-details/SearchDetails';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';

@Injectable()
export class EntriesService {

  private readonly searchUrl = `${environment.baseUrl}/entries/search`;
  private readonly languagesUrl = `${environment.baseUrl}/entries/about/languages`;

  constructor(private http: HttpClient) {
  }

  search(lang: string, query: string): Observable<Search[]> {
    return this.http.get<Search[]>(`${this.searchUrl}/${query}?lang=${lang}`);
  }

  getDetails(lang: string, senseSeq: string): Observable<SearchDetails> {
    return this.http.get<SearchDetails>(`${this.searchUrl}/details/${senseSeq}?lang=${lang}`);
  }

  getLanguages(): Observable<Map<String, String>> {
    return this.http.get<Map<String, String>>(`${this.languagesUrl}`);
  }

}
