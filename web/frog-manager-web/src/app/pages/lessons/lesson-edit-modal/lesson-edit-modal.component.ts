import {Component, HostListener, Input, OnInit} from '@angular/core';
import {InputDto} from '../entities/InputDto';
import {NzModalRef} from 'ng-zorro-antd';
import {AbstractControl, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {LessonsService} from '../../../services/lessons.service';
import {Translation} from '../entities/Translation';

@Component({
  selector: 'app-entry-modal',
  templateUrl: './lesson-edit-modal.component.html',
  styleUrls: ['./lesson-edit-modal.component.css']
})
export class LessonEditModalComponent implements OnInit {

  @Input() inputDto: InputDto;
  @Input() action: string;
  @Input() tags: Array<string>;

  locales = [
    { value: 'fr_FR', src: '../../../assets/flags/france.svg', label: 'Français' },
    { value: 'de_DE', src: '../../../assets/flags/germany.svg', label: 'Allemand' },
    { value: 'it_IT', src: '../../../assets/flags/italy.svg', label: 'Italien' },
    { value: 'es_ES', src: '../../../assets/flags/spain.svg', label: 'Espagnol' },
    {value: 'en_UK', src: '../../../assets/flags/united-kingdom.svg', label: 'Anglais'},
    {value: 'en_US', src: '../../../assets/flags/united-states.svg', label: 'Américain'}
  ];

  flagByLocal = {};
  presentLocales: Array<string> = [];

  validateForm: FormGroup;
  translationForms: Array<TranslationForm> = [];

  errorMessage: string;
  errorDescription: string;

  constructor(private dataService: LessonsService,
              private fb: FormBuilder,
              private modal: NzModalRef) {
  }

  ngOnInit() {
    this.validateForm = this.fb.group({
      kanji: [this.inputDto.japanese.kanji, [this.kanjiValidator]],
      kana: [this.inputDto.japanese.kana, [Validators.required, this.kanaValidator]]
    });

    if (this.action === 'insert') {
      this.addTranslation();
    } else if (this.action === 'update') {
      this.inputDto.translations.forEach(translation => this.addTranslation(null, translation));
    }

    this.locales.forEach(locale => {
      this.flagByLocal[locale.value] = locale.src;
    });
  }

  @HostListener('document:keydown.escape', ['$event'])
  onKeydownHandler() {
    this.modal.close();
  }

  submitForm(): boolean {
    for (const i in this.validateForm.controls) {
      this.validateForm.controls[i].markAsDirty();
      this.validateForm.controls[i].updateValueAndValidity();
    }

    if (this.validateForm.valid) {
      if (this.translationForms.length === 0) {
        this.errorMessage = 'Need at least 1 translation!';
      } else {
        this.upsert();
      }
    } else {
      this.errorMessage = 'Incorrect fields!';
    }

    return false;
  }

  private upsert() {
    this.inputDto.japanese.kanji = this.validateForm.get('kanji').value;
    this.inputDto.japanese.kana = this.validateForm.get('kana').value;

    this.inputDto.translations.forEach(translation => {
      translation.toDelete = true;
    });

    this.translationForms
      .filter(translationForm => !translationForm.created)
      .forEach(translationForm => {
        this.inputDto.translations
          .filter(translation => translation.rowId === translationForm.id)
          .forEach(translation => {
            translation.locale = this.validateForm.get(`locale_${translationForm.id}`).value;
            translation.input = this.validateForm.get(`input_${translationForm.id}`).value;
            translation.details = this.validateForm.get(`details_${translationForm.id}`).value;
            translation.example = this.validateForm.get(`example_${translationForm.id}`).value;
            translation.tags = this.validateForm.get(`tags_${translationForm.id}`).value;
            translation.toDelete = false;
          });
      });

    this.translationForms
      .filter(translationForm => translationForm.created)
      .forEach(translationForm => {
        const translation = new Translation();
        translation.locale = this.validateForm.get(`locale_${translationForm.id}`).value;
        translation.input = this.validateForm.get(`input_${translationForm.id}`).value;
        translation.details = this.validateForm.get(`details_${translationForm.id}`).value;
        translation.example = this.validateForm.get(`example_${translationForm.id}`).value;
        translation.tags = this.validateForm.get(`tags_${translationForm.id}`).value;

        this.inputDto.translations.push(translation);
      });

    this.modal.componentInstance.nzOkLoading = true;
    const next = (data: InputDto) => this.modal.close({ ...data });
    const error = e => this.handleError(e);
    if (this.action === 'insert') {
      this.dataService.insert(this.inputDto).subscribe(next, error);
    } else if (this.action === 'update') {
      this.dataService.update(this.inputDto).subscribe(next, error);
    }
  }

  /**
   * 3000 - 303F: Japanese-style punctuation
   * 3040 - 309F: Hiragana
   * 30A0 - 30FF: Katakana
   * FF00 - FFEF: Full-width Roman characters and half-width Katakana
   * 4E00 - 9FAF: CJK unified ideographs - Common and uncommon Kanji
   * 3400 - 4DBF: CJK unified ideographs Extension A - Rare Kanji
   * FF5E: tild
   * 301C: wave dash
   */
  kanjiValidator = (control: FormControl): { [s: string]: boolean } => {
    if (control.value) {
      const value: string = control.value;
      let isOnlyJapanese = true;
      value.split('').forEach(c => {
        if (!c.match(/[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uffef\u4e00-\u9faf\u3400-\u4dbf]/)) {
          isOnlyJapanese = false;
        }
      });

      if (!isOnlyJapanese) {
        console.error(`not only kanji => ${control.value}`);
        return { isOnlyJapanese: false };
      }
    }
  }

  /**
   * 3000 - 303F: Japanese-style punctuation
   * 3040 - 309F: Hiragana
   * 30A0 - 30FF: Katakana
   * FF00 - FFEF: Full-width Roman characters and half-width Katakana
   * FF5E: tild
   * 301C: wave dash
   */
  kanaValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return { required: true };
    } else {
      const value: string = control.value;
      let isOnlyKana = true;
      value.split('').forEach(c => {
        if (!c.match(/[\u3000-\u303f\u3040-\u309f\u30a0-\u30ff\uff00-\uffef]/)) {
          isOnlyKana = false;
          console.error(`not a kana => ${c}`);
        }
      });

      if (!isOnlyKana) {
        return { isOnlyKana: false };
      }
    }
  }


  showExplain(name: string): boolean {
    const control = this.validateForm.get(name);
    return control.dirty && control.errors != null;
  }

  addTranslation(e?: MouseEvent, translation?: Translation): void {
    if (e) {
      e.preventDefault();
    }

    let id: number;
    let created: boolean;
    if (translation && translation.rowId) {
      id = translation.rowId;
      created = false;
    } else {
      id = (this.translationForms.length > 0) ? this.translationForms[this.translationForms.length - 1].id + 1 : 0;
      created = true;
    }
    const controls = [];

    const locale = new TranslationField('Locale', `locale_${id}`, true);
    controls.push(locale);
    this.validateForm.addControl(locale.name, new FormControl(translation ? translation.locale : null, Validators.required));
    if (translation) {
      this.presentLocales.push(translation.locale);
    }
    const input = new TranslationField('Input', `input_${id}`, true);
    controls.push(input);
    this.validateForm.addControl(input.name, new FormControl(translation ? translation.input : null, Validators.required));

    const details = new TranslationField('Details', `details_${id}`, false);
    controls.push(details);
    this.validateForm.addControl(details.name, new FormControl(translation ? translation.details : null));

    const example = new TranslationField('Example', `example_${id}`, false);
    controls.push(example);
    this.validateForm.addControl(example.name, new FormControl(translation ? translation.example : null));

    const tags = new TranslationField('Tags', `tags_${id}`, false);
    controls.push(tags);
    this.validateForm.addControl(tags.name, new FormControl(translation ? translation.tags : null));

    this.translationForms.push(new TranslationForm(id, controls, created));
  }

  removeTranslation(translation: TranslationForm, index: number, e: MouseEvent): void {

    e.preventDefault();

    this.translationForms.splice(index, 1);

    translation.controls.forEach(control => {
      if (control.name.match(/locale_/)) {
        const locale = this.validateForm.get(control.name).value;
        const findIndex = this.presentLocales.indexOf(locale);
        this.presentLocales.splice(findIndex, 1);
      }
      this.validateForm.removeControl(control.name);
    });
  }

  handleLocalChanged(): void {
    // remove all
    this.presentLocales = [];

    // then redefine all selected locales
    for (const i in this.validateForm.controls) {
      const abstractControl = this.validateForm.controls[i];
      const controlName = this.getControlName(abstractControl);
      if (controlName.match(/locale_/)) {
        this.presentLocales.push(abstractControl.value);
      }
    }
  }

  getControlName(c: AbstractControl): string | null {
    const formGroup = c.parent.controls;
    return Object.keys(formGroup).find(name => c === formGroup[name]) || null;
  }

  afterErrorClose(): void {
    this.errorMessage = undefined;
    this.errorDescription = undefined;
  }

  private handleError(e: any): void {
    this.modal.componentInstance.nzOkLoading = false;
    this.errorMessage = 'Something bad happened; please try again later.';
    this.errorDescription = e;
  }

}

export class TranslationForm {
  id: number;
  controls: Array<TranslationField> = [];
  created: boolean;


  constructor(id: number, controls: Array<TranslationField>, created: boolean) {
    this.id = id;
    this.controls = controls;
    this.created = created;
  }
}

export class TranslationField {
  label: string;
  name: string;
  required: boolean;

  constructor(label: string, name: string, required: boolean) {
    this.label = label;
    this.name = name;
    this.required = required;
  }
}

