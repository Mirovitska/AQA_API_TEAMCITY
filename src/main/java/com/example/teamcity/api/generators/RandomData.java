package com.example.teamcity.api.generators;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomData {

    private static final int LENGTH = 20;


    public static String getString() {
        return "am_" + RandomStringUtils.randomAlphabetic(LENGTH);
    }

    public static String getStringWithSpecialSymbolsAndNumbers() {
        return "am_" + RandomStringUtils.randomAlphabetic(LENGTH) + RandomStringUtils.randomAscii(LENGTH) + RandomStringUtils.randomNumeric(LENGTH);
    }

    public static String getStringExactCountOfChars(int count) {
        return "am_" + RandomStringUtils.randomAlphabetic(count);
    }

    public static String getNumber() {
        return "am_" + RandomStringUtils.randomNumeric(LENGTH);
    }

    public static String getSpecialSymbol() {
        return "@";
    }
}
