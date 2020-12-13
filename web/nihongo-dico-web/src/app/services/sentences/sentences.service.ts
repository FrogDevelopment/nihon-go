import {Injectable} from '@angular/core';

import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Sentence} from './sentence';
import {Observable} from 'rxjs';

@Injectable()
export class SentencesService {

  private readonly searchUrl = `${environment.baseUrl}/sentences/search`;

  constructor(private http: HttpClient) {
  }

  search(lang: string, kanji: string, kana: string, gloss: string): Observable<Sentence[]> {
    return this.http.get<Sentence[]>(`${this.searchUrl}?lang=${lang}&kanji=${kanji}&kana=${kana}&gloss=${gloss}`);
  }
}
