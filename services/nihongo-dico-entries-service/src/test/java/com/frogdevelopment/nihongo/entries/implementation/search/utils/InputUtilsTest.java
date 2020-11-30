package com.frogdevelopment.nihongo.entries.implementation.search.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unitTest")
class InputUtilsTest {

    @Test
    void test_isOnlyHiragana_true() {

        boolean isOnlyHiragana = InputUtils.isOnlyHiragana("しけん");

        Assertions.assertThat(isOnlyHiragana).isTrue();
    }

    @Test
    void test_isOnlyHiragana_false() {

        boolean isOnlyHiragana = InputUtils.isOnlyHiragana("ティーけんてい");

        Assertions.assertThat(isOnlyHiragana).isFalse();
    }

    @Test
    void test_isOnlyKatakana_true() {

        boolean isOnlyKatakana = InputUtils.isOnlyKatakana("テストセット");

        Assertions.assertThat(isOnlyKatakana).isTrue();
    }

    @Test
    void test_isOnlyKatakana_false() {

        boolean isOnlyKatakana = InputUtils.isOnlyKatakana("ティーけんてい");

        Assertions.assertThat(isOnlyKatakana).isFalse();
    }

    @Test
    void test_isOnlyKanji_true() {

        boolean isOnlyKanji = InputUtils.isOnlyKanji("試験");

        Assertions.assertThat(isOnlyKanji).isTrue();
    }

    @Test
    void test_isOnlyKanji_false() {

        boolean isOnlyKanji = InputUtils.isOnlyKanji("t検定");

        Assertions.assertThat(isOnlyKanji).isFalse();
    }

    @Test
    void test_isOnlyJapanese_true() {

        boolean isOnlyJapanese = InputUtils.isOnlyJapanese("知能テスト");

        Assertions.assertThat(isOnlyJapanese).isTrue();
    }

    @Test
    void test_isOnlyJapanese_false() {

        boolean isOnlyJapanese = InputUtils.isOnlyJapanese("DNA鑑定");

        Assertions.assertThat(isOnlyJapanese).isFalse();
    }

    @Test
    void test_isOnlyKana_true() {

        boolean isOnlyKana = InputUtils.isOnlyKana("ティーけんてい");

        Assertions.assertThat(isOnlyKana).isTrue();
    }

    @Test
    void test_isOnlyKana_false() {

        boolean isOnlyKana = InputUtils.isOnlyKana("知能テスト");

        Assertions.assertThat(isOnlyKana).isFalse();
    }

    @Test
    void test_containsJapanese_true() {

        boolean isContainsJapanese = InputUtils.containsJapanese("視る");

        Assertions.assertThat(isContainsJapanese).isTrue();
    }

    @Test
    void test_containsJapanese_false() {

        boolean isContainsJapanese = InputUtils.containsJapanese("miru");

        Assertions.assertThat(isContainsJapanese).isFalse();
    }

    @Test
    void test_containsNoJapanese_true() {

        boolean containsNoJapanese = InputUtils.containsNoJapanese("miru");

        Assertions.assertThat(containsNoJapanese).isTrue();
    }

    @Test
    void test_containsNoJapanese_false() {

        boolean containsNoJapanese = InputUtils.containsNoJapanese("視る");

        Assertions.assertThat(containsNoJapanese).isFalse();
    }

    @Test
    void test_containsKanji_true() {

        boolean containsKanji = InputUtils.containsKanji("視る");

        Assertions.assertThat(containsKanji).isTrue();
    }

    @Test
    void test_containsKanji_false() {

        boolean containsKanji = InputUtils.containsKanji("みる");

        Assertions.assertThat(containsKanji).isFalse();
    }

}
