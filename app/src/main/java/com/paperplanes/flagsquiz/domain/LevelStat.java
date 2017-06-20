package com.paperplanes.flagsquiz.domain;

/**
 * Created by abdularis on 09/05/17.
 */

public class LevelStat {

    private int mFlagsCount;
    private int mAnsweredFlagsCount;

    public LevelStat() {
        this(0, 0);
    }

    public LevelStat(int flagsCount, int answeredFlagsCount) {
        mFlagsCount = flagsCount;
        mAnsweredFlagsCount = answeredFlagsCount;
    }

    public int getFlagsCount() {
        return mFlagsCount;
    }

    public void setFlagsCount(int flagsCount) {
        mFlagsCount = flagsCount;
    }

    public int getAnsweredFlagsCount() {
        return mAnsweredFlagsCount;
    }

    public void setAnsweredFlagsCount(int answeredFlagsCount) {
        mAnsweredFlagsCount = answeredFlagsCount;
    }
}
