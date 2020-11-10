import {Japanese} from './Japanese';
import {Translation} from './Translation';

export class InputDto {
  expand: boolean;
  japanese: Japanese;
  translations: Array<Translation> = [];
}
