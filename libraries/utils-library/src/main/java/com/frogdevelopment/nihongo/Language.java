package com.frogdevelopment.nihongo;

import lombok.Getter;

public enum Language {
    ENG("eng"),
    DUT("dut"),
    FRA("fra"),
    GER("ger"),
    HUN("hun"),
    RUS("rus"),
    SPA("spa"),
    SWE("swe"),
    SLV("slv");

    @Getter
    private final String code;

    Language(String code) {
        this.code = code;
    }
}
