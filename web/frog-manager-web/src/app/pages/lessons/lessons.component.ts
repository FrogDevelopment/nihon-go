import {Component, OnInit} from '@angular/core';
import {LessonsService} from '../../services/lessons.service';
import {InputDto} from './entities/InputDto';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {Japanese} from './entities/Japanese';
import {LessonEditModalComponent} from './lesson-edit-modal/lesson-edit-modal.component';

@Component({
  selector: 'app-data-edition',
  templateUrl: './lessons.component.html',
  providers: [LessonsService],
  styleUrls: ['./lessons.component.css']
})
export class LessonsComponent implements OnInit {
  pageIndex = 1;
  pageSize = 20;
  total = 1;
  entries: InputDto[] = [];
  loading = false;

  sortOrder = null;
  sortField = null;

  flagByLocal = {
    fr_FR: '../../assets/flags/france.svg',
    de_DE: '../../assets/flags/germany.svg',
    it_IT: '../../assets/flags/italy.svg',
    es_ES: '../../assets/flags/spain.svg',
    en_UK: '../../assets/flags/united-kingdom.svg',
    en_US: '../../assets/flags/united-states.svg'
  };

  tags: Array<string>;
  colorByTags = {};

  constructor(private dataService: LessonsService,
              private message: NzMessageService,
              private modalService: NzModalService) {

  }

  ngOnInit(): void {
    this.getTags();
  }

  sortKanji = (a: InputDto, b: InputDto) => a.japanese.kanji.localeCompare(b.japanese.kanji);
  sortKana = (a: InputDto, b: InputDto) => a.japanese.kana.localeCompare(b.japanese.kana);

  sort(sort: { key: string, value: string }): void {
    this.sortOrder = sort.value;
    if (!sort.value) {
      this.sortField = null;
    } else {
      this.sortField = sort.key;
    }
    this.searchData();
  }

  getTags(): void {
    this.loading = true;


    this.dataService.getTags()
      .subscribe(
        tags => {
          this.tags = tags;

          this.computeColorTags();

          this.searchTotal();
        },
        () => this.handleError());
  }

  searchTotal(): void {
    this.loading = true;
    this.dataService.getTotal()
      .subscribe(
        total => {
          this.total = total;
          this.searchData(true);
        },
        () => this.handleError());
  }

  searchData(reset: boolean = false): void {
    if (reset) {
      this.pageIndex = 1;
    }
    this.loading = true;
    this.dataService.getDtos(this.pageIndex, this.pageSize, this.sortField, this.sortOrder)
      .subscribe(
        data => {
          this.loading = false;
          data.forEach((dto: InputDto) => {
            dto.expand = true;
          });
          this.entries = data;
        },
        () => this.handleError());
  }

  addEntry(): void {
    const dto = new InputDto();
    dto.japanese = new Japanese();

    const modal = this.modalService.create({
      nzTitle: 'Add a new entry',
      nzContent: LessonEditModalComponent,
      nzClosable: false,
      nzMaskClosable: false,
      nzComponentParams: {
        inputDto: dto,
        tags: this.tags,
        action: 'insert'
      },
      nzFooter: [
        {
          label: 'CANCEL',
          type: 'default',
          onClick: () => modal.close()
        },
        {
          label: 'SAVE',
          type: 'primary',
          onClick: (componentInstance) => componentInstance.submitForm()
        }
      ]
    });

    // add new dto (fixme find position)
    modal.afterClose.subscribe((result: InputDto) => {
      if (result) {
        result.expand = true;
        this.entries.unshift(result);
        this.total++;
      }
    });
  }

  editEntry(dto: InputDto, index: number): void {
    const modal = this.modalService.create({
      nzTitle: 'Edit entry',
      nzContent: LessonEditModalComponent,
      nzClosable: false,
      nzMaskClosable: false,
      nzComponentParams: {
        inputDto: dto,
        tags: this.tags,
        action: 'update'
      },
      nzFooter: [
        {
          label: 'CANCEL',
          type: 'default',
          onClick: () => modal.close()
        },
        {
          label: 'UPDATE',
          type: 'primary',
          onClick: (componentInstance) => componentInstance.submitForm()
        }]
    });

    // replace dto
    modal.afterClose.subscribe((result: InputDto) => {
      if (result) {
        result.expand = true;
        this.entries[index] = result;
      }
    });

  }

  delete(dto: InputDto): void {
    this.loading = true;
    this.dataService.delete(dto)
      .subscribe(
        () => {
          this.total--;
          this.searchData(true);
        },
        () => this.handleError());
  }

  private handleError(): void {
    this.loading = false;
    this.message.error('Something bad happened; please try again later.');
  }

  private computeColorTags() {
    for (const i in this.tags) {
      this.colorByTags[this.tags[i]] = '#' + Math.floor(Math.random() * 16777215).toString(16);
    }
  }

}
