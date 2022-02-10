import {Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Singleton({
  selector: 'app-search-settings',
  templateUrl: './search-settings.component.html',
  styleUrls: ['./search-settings.component.css']
})
export class SearchSettingsComponent implements OnInit {

  settingsForm: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              private fb: FormBuilder) {
    this.settingsForm = this.fb.group({
      lang: [this.data.searchLang, Validators.required]
    });
  }

  ngOnInit() {
    this.settingsForm.get('lang').setValue(this.data.searchLang);
  }

}
