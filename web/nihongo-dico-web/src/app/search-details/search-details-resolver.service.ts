import 'rxjs/add/operator/map';
import 'rxjs/add/operator/take';
import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from '@angular/router';

import {SearchDetails} from './SearchDetails';
import {EntriesService} from '../_services/entries.service';
import {catchError, mergeMap, take} from 'rxjs/operators';
import {EMPTY, Observable, of} from 'rxjs';

// cf https://angular.io/guide/router#fetch-data-before-navigating

@Injectable()
export class SearchDetailResolver implements Resolve<SearchDetails> {
  constructor(private searchService: EntriesService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<SearchDetails> {

    const lang = route.paramMap.get('lang');
    const senseSeq = route.paramMap.get('senseSeq');

    return this.searchService.getDetails(lang, senseSeq).pipe(
      take(1),
      mergeMap(details => of(details)),
      catchError(() => EMPTY)
    );
  }
}
