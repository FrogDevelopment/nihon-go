package fr.frogdevelopment.nihongo.sentence.implementation.search.entity;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sentence implements Serializable {

    private static final long serialVersionUID = 3L;

    private String japanese;
    private String translation;

}
