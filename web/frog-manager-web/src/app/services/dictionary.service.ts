import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../environments/environment';


@Injectable({
  providedIn: 'root'
})
export class DictionaryService {

  constructor(private http: HttpClient) {
  }

  getDictionaryAbout(): Observable<Map<string, number>> {
    return this.http
      .get<Map<string, number>>(`${environment.baseUrl}/nihongo/dico/entries/about/languages`);
  }

  synchronizeDictionary(): Observable<any> {
    return this.http
      .get<any>(`${environment.baseUrl}/nihongo/dico/entries/populate`);
  }


  getSentencesAbout(): Observable<Map<string, number>> {
    return this.http
      .get<Map<string, number>>(`${environment.baseUrl}/nihongo/dico/sentences/about/languages`);
  }

  synchronizeSentences(): Observable<any> {
    return this.http
      .get<any>(`${environment.baseUrl}/nihongo/dico/sentences/populate`);
  }

}
