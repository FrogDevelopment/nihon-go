package com.frogdevelopment.nihongo.lessons.entity;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Map;

import io.micronaut.core.annotation.Introspected;

@Value
@Builder
@Introspected
public class InputDto {

    Japanese japanese;
    @Singular("translation")
    Map<String, Translation> translations;
}
