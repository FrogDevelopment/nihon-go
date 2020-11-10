package fr.frogdevelopment.nihongo.lesson.dao;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Input implements Serializable {

    private String kanji;
    private String kana;
    private String sortLetter;
    private String input;
    private String details;
    private String example;
    private String tags;
}
