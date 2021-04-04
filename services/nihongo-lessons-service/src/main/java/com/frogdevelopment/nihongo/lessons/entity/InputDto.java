package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class InputDto {

    @NonNull
    Japanese japanese;
    @NonNull
    @Singular("translation")
    List<Translation> translations;
}
