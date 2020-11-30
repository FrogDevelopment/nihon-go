import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';

import {SearchDetails} from './SearchDetails';
import {catchError, map, switchMap, take, tap} from 'rxjs/operators';
import {EMPTY, Observable} from 'rxjs';
import {EntriesService} from '../../services/entries';
import {Sentence, SentencesService} from '../../services/sentences';

@Injectable()
export class SearchDetailResolver implements Resolve<SearchDetails> {

  constructor(private searchService: EntriesService,
              private sentencesService: SentencesService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<SearchDetails> {

    const lang = route.paramMap.get('lang');
    const senseSeq = route.paramMap.get('senseSeq');

    let resolvedDetails: SearchDetails;
    return this.searchService.getDetails(lang, senseSeq).pipe(
      take(1),
      tap((details: SearchDetails) => resolvedDetails = details),
      switchMap((details: SearchDetails) => this.sentencesService.search(lang, details.kanji, details.kana, details.gloss)),
      tap((sentences: Sentence[]) => resolvedDetails.sentences = sentences),
      map(() => resolvedDetails),
      catchError(() => EMPTY)
    );
  }
}
