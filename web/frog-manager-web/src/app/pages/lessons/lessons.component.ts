import {Component, OnInit} from '@angular/core';
import {LessonsService} from '../../services/lessons.service';
import {InputDto} from './entities/InputDto';
import {Japanese} from './entities/Japanese';
import {LessonEditModalComponent} from './lesson-edit-modal/lesson-edit-modal.component';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzModalService} from 'ng-zorro-antd/modal';
import {NzTableSortFn} from 'ng-zorro-antd/table';

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

  constructor(private dataService: LessonsService,
              private message: NzMessageService,
              private modalService: NzModalService) {

  }

  ngOnInit(): void {
    this.searchTotal();
  }

  sortKanji: NzTableSortFn<InputDto> = (a: InputDto, b: InputDto) => a.japanese.kanji.localeCompare(b.japanese.kanji);
  sortKana: NzTableSortFn<InputDto> = (a: InputDto, b: InputDto) => a.japanese.kana.localeCompare(b.japanese.kana);
  sortLesson: NzTableSortFn<InputDto> = (a: InputDto, b: InputDto) => a.japanese.lesson - b.japanese.lesson;

  sort(sort: { key: string, value: string }): void {
    this.sortOrder = sort.value;
    if (!sort.value) {
      this.sortField = null;
    } else {
      this.sortField = sort.key;
    }
    this.searchData();
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
    this.entries = [];
    this.dataService.getDtos(this.pageIndex, this.pageSize, this.sortField, this.sortOrder)
      .subscribe(
        data => {
          this.loading = false;
          // data.forEach((dto: InputDto) => {
          //   dto.expand = true;
          // });
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
        this.entries = [...this.entries, result];
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

  delete(dto: InputDto, index: number): void {
    this.loading = true;
    this.dataService.delete(dto)
      .subscribe(
        () => {
          this.total--;
          this.entries.splice(index, 1);
          this.entries = [...this.entries];
          // this.searchData(true);
        },
        () => this.handleError());
  }

  private handleError(): void {
    this.loading = false;
    this.message.error('Something bad happened; please try again later.');
  }

}
