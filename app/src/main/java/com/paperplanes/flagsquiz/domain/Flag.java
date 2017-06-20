package com.paperplanes.flagsquiz.domain;

/**
 * Created by abdularis on 07/05/17.
 */

public class Flag {

    private FlagDetail mFlagDetail;
    private boolean mIsAnswered;

    public Flag(FlagDetail flagDetail, boolean isAnswered) {
        mFlagDetail = flagDetail;
        mIsAnswered = isAnswered;
    }

    public FlagDetail getFlagDetail() {
        return mFlagDetail;
    }

    public void setFlagDetail(FlagDetail flagDetail) {
        mFlagDetail = flagDetail;
    }

    public boolean isAnswered() {
        return mIsAnswered;
    }

    public void setAnswered(boolean answered) {
        mIsAnswered = answered;
    }
}
