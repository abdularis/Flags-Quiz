package com.paperplanes.flagsquiz.domain;

/**
 * Created by abdularis on 17/05/17.
 */

public class ContinentHint extends Hint {
    public static final String NAME = "hint.continent";
    public static final int PRICE = 25;

    public ContinentHint(Flag flag) {
        super(flag, PRICE, NAME);
    }

    public String getContinent() {
        return getFlag().getFlagDetail().getContinent();
    }
}
