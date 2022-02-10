package com.frogdevelopment.nihongo.lessons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.Normalizer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {

    @NotNull
    public static String getSortLetter(final String input) {
        // Normalizer.normalize(source, Normalizer.Form.NFD) renvoi une chaine unicode décomposé.
        // C'est à dire que les caractères accentués seront décomposé en deux caractères (par exemple "à" se transformera en "a`").
        // Le replaceAll("[\u0300-\u036F]", "") supprimera tous les caractères unicode allant de u0300 à u036F,
        // c'est à dire la plage de code des diacritiques (les accents qu'on a décomposé ci-dessus donc).
        return Normalizer
                .normalize(StringUtils.trimToEmpty(input).substring(0, 1), Normalizer.Form.NFD)
                .replaceAll("[\u0300-\u036F]", "")
                .toUpperCase();
    }
}
