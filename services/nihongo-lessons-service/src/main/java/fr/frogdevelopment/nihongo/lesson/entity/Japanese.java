package fr.frogdevelopment.nihongo.lesson.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Japanese implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String kanji;
    private String kana;

}
