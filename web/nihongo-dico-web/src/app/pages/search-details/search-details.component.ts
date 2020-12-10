import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {SearchDetails} from './SearchDetails';
import {EntriesService} from '../../services/entries';
import {SentencesService} from '../../services/sentences';
import {switchMap, tap} from 'rxjs/operators';

@Component({
  selector: 'app-details',
  templateUrl: './search-details.component.html',
  styleUrls: ['./search-details.component.css']
})
export class SearchDetailsComponent implements OnInit {

  details: SearchDetails;

  constructor(private activatedRoute: ActivatedRoute,
              private entriesService: EntriesService,
              private sentencesService: SentencesService,
              private location: Location) {
  }

  goBack(): void {
    this.location.back();
  }

  ngOnInit() {
    // this.activatedRoute.data
    //   .subscribe((data: { details: SearchDetails }) => {
    //   const lang = this.activatedRoute.snapshot.paramMap.get('lang');
    //   this.sentencesService.search(lang, data.details.kanji, data.details.kana)
    //     .subscribe(sentences => {
    //       data.details.sentences = sentences;
    //     });
    //
    //   return this.details = data.details;
    // });
    const lang = this.activatedRoute.snapshot.paramMap.get('lang');
    this.activatedRoute.data
      .pipe(
        tap((data: { details: SearchDetails }) => this.details = data.details),
        switchMap((data: { details: SearchDetails }) => this.sentencesService.search(lang, data.details.kanji, data.details.kana))
      ).subscribe(sentences => this.details.sentences = sentences);
  }

}
