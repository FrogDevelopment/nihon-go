package com.frogdevelopment.nihongo.lesson.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Translation implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int japaneseId;
    private String locale;
    private String input;
    private String sortLetter;
    private String details;
    private String example;
    @Singular("tag")
    private List<String> tags;

    private boolean toDelete;

    public String computeSortLetter() {
        // Normalizer.normalize(source, Normalizer.Form.NFD) renvoi une chaine unicode décomposé.
        // C'est à dire que les caractères accentués seront décomposé en deux caractères (par exemple "à" se transformera en "a`").
        // Le replaceAll("[\u0300-\u036F]", "") supprimera tous les caractères unicode allant de u0300 à u036F,
        // c'est à dire la plage de code des diacritiques (les accents qu'on a décomposé ci-dessus donc).
        String sortLetter = Normalizer.normalize(input.substring(0, 1), Normalizer.Form.NFD)
                .replaceAll("[\u0300-\u036F]", "");

        this.sortLetter = sortLetter.toUpperCase();

        return this.sortLetter;
    }
}
