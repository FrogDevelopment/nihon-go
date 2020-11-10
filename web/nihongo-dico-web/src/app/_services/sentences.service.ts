import {Injectable} from '@angular/core';

import {Observable} from 'rxjs/Observable';

import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Sentence} from '../search-details/Sentence';

@Injectable()
export class SentencesService {

  private searchUrl = `${environment.baseUrl}/sentences/search`;

  constructor(private http: HttpClient) {
  }

  search(lang: string, kanji: string, kana: string): Observable<Sentence[]> {
    return this.http.get<Sentence[]>(`${this.searchUrl}?lang=${lang}&kanji=${kanji}&kana=${kana}`);
  }
}
