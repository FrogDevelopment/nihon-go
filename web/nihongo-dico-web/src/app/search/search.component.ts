import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {EntriesService} from '../_services/entries.service';
import {Search} from './Search';
import {FormBuilder, FormGroup} from '@angular/forms';
import {Router} from '@angular/router';
import {PlatformLocation} from '@angular/common';
import {MatDialog} from '@angular/material';
import {SearchSettingsComponent} from '../search-settings/search-settings.component';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  searchForm: FormGroup;
  searchLang = 'eng';
  results: Search[];
  showLoading = false;
  languages: Array<any>;

  scrollPosition: number;

  @ViewChild('list_content', {static: false, read: ElementRef}) public listContent: ElementRef;

  constructor(private dialog: MatDialog,
              private searchService: EntriesService,
              private fb: FormBuilder,
              private router: Router,
              private location: PlatformLocation) {
    this.searchForm = this.fb.group({
      query: ['']
    });

    location.onPopState(() => {
      setTimeout(() => {
        this.listContent.nativeElement.scrollTop = this.scrollPosition;
      });
      return this.showLoading = false;
    });
  }

  ngOnInit() {
    this.searchService.getLanguages().subscribe(
      languages => {
        this.languages = [];
        if (languages) {
          Object.keys(languages).forEach(key => {
            this.languages.push({code: key, count: +languages[key]});
          });
        } else {
          this.languages.push({code: 'eng', count: 0});
        }
        this.languages.sort((a, b) => a.count > b.count ? -1 : a.count < b.count ? 1 : 0);

        return this.languages;
      },
      () => this.languages.push({code: 'eng', count: 0})
    );
  }

  showSettings(): void {
    const dialogRef = this.dialog.open(SearchSettingsComponent, {
      width: '400px',
      data: {
        languages: this.languages,
        searchLang: this.searchLang
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result == null) {
        return;
      }
      return this.searchLang = result;
    });
  }

  search(): void {
    const query: string = this.searchForm.get('query').value.trim();

    if (!query) {
      return;
    }

    this.showLoading = true;
    this.searchService.search(this.searchLang, query).subscribe(data => {
      this.showLoading = false;
      setTimeout(() => this.listContent.nativeElement.scrollTop = 0);
      return this.results = data;
    });
  }

  onShowDetails(row): void {
    if (!this.showLoading) {
      this.showLoading = true;

      this.scrollPosition = this.listContent.nativeElement.scrollTop;

      this.router.navigate(['/details/', row.senseSeq, this.searchLang])
        .then(fulfilled => {
          if (!fulfilled) {
            this.showLoading = false;
          }
        });
    }
  }

  clear(): void {
    this.searchForm.get('query').setValue('');
    this.results = [];
  }

}
