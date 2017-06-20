package com.paperplanes.flagsquiz.domain;

/**
 * Created by abdularis on 17/05/17.
 */

public class HalfAnswerHint extends Hint {
    public static final String NAME = "hint.halfanswer";
    public static final int PRICE = 100;

    public HalfAnswerHint(Flag flag) {
        super(flag, PRICE, NAME);
    }

    public String getHalfName() {
        String country = getFlag().getFlagDetail().getCountry();
        return country.substring(0, country.length() / 2);
    }
}
