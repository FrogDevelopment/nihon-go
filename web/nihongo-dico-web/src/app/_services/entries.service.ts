import {Injectable} from '@angular/core';

import {Observable} from 'rxjs/Observable';

import {HttpClient} from '@angular/common/http';
import {Search} from '../search/Search';
import {SearchDetails} from '../search-details/SearchDetails';
import {environment} from '../../environments/environment';

@Injectable()
export class EntriesService {

  private searchUrl = `${environment.baseUrl}/entries/search`;
  private languagesUrl = `${environment.baseUrl}/entries/about/languages`;

  constructor(private http: HttpClient) {
  }

  search(lang: string, query: string): Observable<Search[]> {
    return this.http.get<Search[]>(`${this.searchUrl}?query=${query}&lang=${lang}`);
  }

  getDetails(lang: string, senseSeq: string): Observable<SearchDetails> {
    return this.http.get<SearchDetails>(`${this.searchUrl}/details?senseSeq=${senseSeq}&lang=${lang}`);
  }

  getLanguages(): Observable<Map<String, String>> {
    return this.http.get<Map<String, String>>(`${this.languagesUrl}`);
  }

}
