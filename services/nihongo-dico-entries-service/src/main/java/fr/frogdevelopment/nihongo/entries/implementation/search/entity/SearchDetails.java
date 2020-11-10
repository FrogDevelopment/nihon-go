package fr.frogdevelopment.nihongo.entries.implementation.search.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class SearchDetails implements Serializable {

    private static final long serialVersionUID = 3L;

    private long entrySeq;
    private String kanji;
    private String kana;
    private String reading;
    private Set<String> pos;
    private Set<String> field;
    private Set<String> misc;
    private String info;
    private Set<String> dial;
    private String gloss;
}
