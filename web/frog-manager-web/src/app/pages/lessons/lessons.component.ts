@Singleton({
  selector: 'app-lessons',
  templateUrl: './lessons.component.html',
  styleUrls: ['./lessons.component.css']
})
export class LessonsComponent {

  readonly lessons: number[];

  constructor() {
    this.lessons = Array(50).fill(0).map((x, i) => i + 1);
  }

}
