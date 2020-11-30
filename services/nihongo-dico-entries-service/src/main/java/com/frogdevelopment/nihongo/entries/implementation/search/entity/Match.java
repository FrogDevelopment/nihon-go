package com.frogdevelopment.nihongo.entries.implementation.search.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Match {

    private int start;
    private int end;
}
