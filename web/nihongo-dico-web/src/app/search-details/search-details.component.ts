import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {EntriesService} from '../_services/entries.service';
import {Location} from '@angular/common';
import {SearchDetails} from './SearchDetails';
import {SentencesService} from '../_services/sentences.service';

@Component({
  selector: 'app-details',
  templateUrl: './search-details.component.html',
  styleUrls: ['./search-details.component.css']
})
export class SearchDetailsComponent implements OnInit {

  details: SearchDetails;

  constructor(private route: ActivatedRoute,
              private searchService: EntriesService,
              private sentencesService: SentencesService,
              private location: Location) {
  }

  ngOnInit() {
    this.route.data.subscribe((data: { details: SearchDetails }) => {
      const lang = this.route.snapshot.paramMap.get('lang');
      this.sentencesService.search(lang, data.details.kanji, data.details.kana)
        .subscribe(sentences => {
          data.details.sentences = sentences;
        });

      return this.details = data.details;
    });
  }

  goBack(): void {
    this.location.back();
  }

}
