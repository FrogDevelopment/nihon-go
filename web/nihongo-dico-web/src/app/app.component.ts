import {Component} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {MatIconRegistry} from '@angular/material/icon';
import {AboutComponent} from './pages/about/about.component';
import {TranslateService} from '@ngx-translate/core';
import {DomSanitizer} from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  constructor(private dialog: MatDialog,
              public translate: TranslateService,
              private matIconRegistry: MatIconRegistry,
              private domSanitizer: DomSanitizer) {

    const langs = ['eng', 'fra'];
    translate.addLangs(langs);

    // this language will be used as a fallback when a translation isn't found in the current language
    translate.setDefaultLang('eng');

    // the lang to use, if the lang isn't available, it will use the current loader to get them
    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/eng|fra/) ? browserLang : 'eng');

    // fixme add credit for icons => https://www.iconfinder.com/iconsets/ensign-11
    // langs.forEach(value => {
    //   const iconName = `flag_${value}`;
    //   this.matIconRegistry.addSvgIcon(
    //     iconName,
    //     this.domSanitizer.bypassSecurityTrustResourceUrl(`assets/icons/${iconName}.svg`)
    //   );
    // });
  }

  showAbout(): void {
    this.dialog.open(AboutComponent, {
      width: '400px'
    });
  }
}
