import {Sentence} from './Sentence';

export class SearchDetails {

  kanji: string;
  kana: string;
  reading: string;
  pos: string[];
  field: string[];
  misc: string[];
  info: string;
  dial: string[];
  gloss: string[];

  sentences: Sentence[];
}