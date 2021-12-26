package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Map;

@Value
@Builder
public class InputDto {

    Japanese japanese;
    @Singular("translation")
    Map<String, Translation> translations;
}
