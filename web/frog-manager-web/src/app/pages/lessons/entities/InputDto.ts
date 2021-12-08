import {Japanese} from './Japanese';
import {Translation} from './Translation';

export class InputDto {
  japanese: Japanese;
  translations: Map<string, Translation>;
}
