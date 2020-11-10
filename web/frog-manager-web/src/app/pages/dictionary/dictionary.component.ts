import {Component, OnInit} from '@angular/core';
import {DictionaryService} from '../../services/dictionary.service';

@Component({
  selector: 'app-dictionary',
  templateUrl: './dictionary.component.html',
  styleUrls: ['./dictionary.component.css']
})
export class DictionaryComponent implements OnInit {

  dictionaryAbout: Map<string, number>;
  dictionaryAnimate = false;
  dictionaryLoading = false;

  sentencesAbout: Map<string, number>;
  sentencesAnimate = false;
  sentencesLoading = false;

  constructor(private dictionaryService: DictionaryService) {
  }

  ngOnInit() {
    this.getDictionaryAbout();
    this.getSentencesAbout();
  }

  getDictionaryAbout() {
    this.dictionaryLoading = true;
    this.dictionaryService.getDictionaryAbout().subscribe(
      about => {
        this.dictionaryLoading = false;
        return this.dictionaryAbout = about;
      });
  }

  getSentencesAbout() {
    this.sentencesLoading = true;
    this.dictionaryService.getSentencesAbout().subscribe(
      about => {
        this.sentencesLoading = false;
        return this.sentencesAbout = about;
      });
  }

  synchronizeDictionary(): void {
    this.dictionaryAnimate = true;
    this.dictionaryService.synchronizeDictionary().subscribe(
      () => {
        this.getDictionaryAbout();
        this.dictionaryAnimate = false;
      },
      () => this.dictionaryAnimate = false);
  }

  synchronizeSentences(): void {
    this.sentencesAnimate = true;
    this.dictionaryService.synchronizeSentences().subscribe(
      () => {
        this.getSentencesAbout();
        this.sentencesAnimate = false;
      },
      () => this.sentencesAnimate = false);
  }
}
