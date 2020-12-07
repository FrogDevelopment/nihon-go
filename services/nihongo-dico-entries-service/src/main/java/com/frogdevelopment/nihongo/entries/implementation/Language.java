package com.frogdevelopment.nihongo.entries.implementation;

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
    SLV("slv"),
    ITA("ita");

    @Getter
    private final String code;

    Language(String code) {
        this.code = code;
    }
}
