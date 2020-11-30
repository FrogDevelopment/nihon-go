import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';
import {SearchDetails} from './SearchDetails';
import {EntriesService} from '../../services/entries';
import {SentencesService} from '../../services/sentences';

@Component({
  selector: 'app-details',
  templateUrl: './search-details.component.html',
  styleUrls: ['./search-details.component.css']
})
export class SearchDetailsComponent {

  details: SearchDetails;

  constructor(private activatedRoute: ActivatedRoute,
              private entriesService: EntriesService,
              private sentencesService: SentencesService,
              private location: Location) {
    this.activatedRoute.data.subscribe((data: { details: SearchDetails }) => this.details = data.details);
  }

  goBack(): void {
    this.location.back();
  }

}
