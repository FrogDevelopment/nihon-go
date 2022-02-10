import {OnInit} from '@angular/core';
import {LessonsService} from '../../../services/lessons.service';
import {InputDto} from '../entities/InputDto';
import {Japanese} from '../entities/Japanese';
import {LessonEditModalComponent} from '../lesson-edit-modal/lesson-edit-modal.component';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzModalService} from 'ng-zorro-antd/modal';
import {NzTableSortFn} from 'ng-zorro-antd/table';
import {ActivatedRoute} from '@angular/router';
import {ExportService, LessonInfo} from '../../../services/export.service';

@Singleton({
  selector: 'app-lesson-content',
  templateUrl: './lesson-content.component.html',
  providers: [LessonsService],
  styleUrls: ['./lesson-content.component.css']
})
export class LessonContentComponent implements OnInit {
  lessonInfo: LessonInfo;
  entries: InputDto[] = [];
  loading = false;
  exportLoading = false;
  lesson = 1;

  sortOrder = null;
  sortField = null;

  readonly locales = ['en_US', 'fr_FR'];
  readonly flagByLocal = {
    de_DE: '../../assets/flags/germany.svg',
    en_UK: '../../assets/flags/united-kingdom.svg',
    en_US: '../../assets/flags/united-states.svg',
    es_ES: '../../assets/flags/spain.svg',
    fr_FR: '../../assets/flags/france.svg',
    it_IT: '../../assets/flags/italy.svg'
  };

  constructor(private lessonsService: LessonsService,
              private exportService: ExportService,
              private nzMessageService: NzMessageService,
              private nzModalService: NzModalService,
              private activatedRoute: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe(value => {
      this.lesson = Number(value.get('id'));
      this.searchData();
    });
  }

  sortKanji: NzTableSortFn<InputDto> = (a: InputDto, b: InputDto) => a.japanese.kanji.localeCompare(b.japanese.kanji);
  sortKana: NzTableSortFn<InputDto> = (a: InputDto, b: InputDto) => a.japanese.kana.localeCompare(b.japanese.kana);

  sort(sort: { key: string, value: string }): void {
    this.sortOrder = sort.value;
    if (!sort.value) {
      this.sortField = null;
    } else {
      this.sortField = sort.key;
    }
    this.searchData();
  }

  searchData(): void {
    if (this.lesson === undefined) {
      return;
    }

    this.loading = true;

    this.getInfo();
    this.entries = [];
    this.lessonsService.getDtos(this.lesson, this.sortField, this.sortOrder)
      .subscribe(
        data => {
          this.loading = false;
          this.entries = data;
        },
        () => this.handleError());
  }

  addEntry(): void {
    const dto = new InputDto();
    dto.japanese = new Japanese();

    const modal = this.nzModalService.create({
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

    modal.afterClose.subscribe((result: InputDto) => {
      if (result) {
        this.nzMessageService.success('Data added');
        this.searchData();
      }
    });
  }

  editEntry(dto: InputDto): void {
    const modal = this.nzModalService.create({
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

    modal.afterClose.subscribe((result: InputDto) => {
      if (result) {
        this.nzMessageService.success('Data updated');
        this.searchData();
      }
    });
  }

  delete(dto: InputDto): void {
    this.loading = true;
    this.lessonsService.delete(dto)
      .subscribe(
        () => {
          this.nzMessageService.success('Data deleted');
          this.searchData();
        },
        () => this.handleError());
  }

  getInfo(): void {
    this.lessonInfo = undefined;
    this.exportService.getInfo(this.lesson)
      .subscribe(
        result => this.lessonInfo = result,
        () => this.handleError()
      );
  }

  exportLesson(): void {
    this.exportLoading = true;
    this.exportService.export(this.lesson)
      .subscribe(
        () => {
          this.exportLoading = false;
          this.nzMessageService.success('Export done');
          this.getInfo();
        },
        () => this.handleError()
      );
  }

  private handleError(): void {
    this.loading = false;
    this.nzMessageService.error('Something bad happened; please try again later.');
  }

}
