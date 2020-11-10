package fr.frogdevelopment.nihongo.entries.implementation.search.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class SearchResult implements Serializable {

    private static final long serialVersionUID = 3L;

    private String kanji;
    private String kana;
    private String reading;
    private String senseSeq;
    private String vocabulary;
    private double similarity;

}
