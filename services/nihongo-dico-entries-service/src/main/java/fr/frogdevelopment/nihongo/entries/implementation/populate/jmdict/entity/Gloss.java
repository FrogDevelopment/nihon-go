package fr.frogdevelopment.nihongo.entries.implementation.populate.jmdict.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Gloss {

    private String lang;
    private String value;
}
