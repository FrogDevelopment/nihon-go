package com.frogdevelopment.nihongo.entries.implementation;

import lombok.Getter;

public enum Language {
    ENG("eng"),
    DUT("dut"),
    FRE("fre"),
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
