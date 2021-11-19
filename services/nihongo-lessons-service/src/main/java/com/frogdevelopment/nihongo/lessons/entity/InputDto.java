package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class InputDto {

    Japanese japanese;
    @Singular("translation")
    List<Translation> translations;
}
