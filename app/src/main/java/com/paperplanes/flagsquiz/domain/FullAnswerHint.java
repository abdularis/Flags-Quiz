package com.paperplanes.flagsquiz.domain;

/**
 * Created by abdularis on 17/05/17.
 */

public class FullAnswerHint extends Hint {
    public static final String NAME = "hint.fullanswer";
    private static final int PRICE = 250;

    public FullAnswerHint(Flag flag) {
        super(flag, PRICE, NAME);
    }

    public String getFullAnswer() {
        return getFlag().getFlagDetail().getCountry();
    }
}
