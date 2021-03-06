package com.frogdevelopment.nihongo.sentences.implementation.search.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
public class Sentence implements Serializable {

    @Serial
    private static final long serialVersionUID = 3L;

    private String japanese;
    private String translation;

}
