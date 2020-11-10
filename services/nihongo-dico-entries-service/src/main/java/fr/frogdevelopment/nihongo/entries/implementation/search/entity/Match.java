package fr.frogdevelopment.nihongo.entries.implementation.search.entity;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Match implements Serializable {

    private static final long serialVersionUID = 2L;

    private int start;
    private int end;
}
