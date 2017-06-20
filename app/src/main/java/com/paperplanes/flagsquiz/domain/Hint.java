package com.paperplanes.flagsquiz.domain;

/**
 * Created by abdularis on 17/05/17.
 */

public abstract class Hint {

    private Flag mFlag;
    private boolean mIsBought;
    private int mPrice;
    private String mName;

    public Hint(Flag flag, int price, String name) {
        mFlag = flag;
        mIsBought = false;
        mPrice = price;
        mName = name;
    }

    public Flag getFlag() {
        return mFlag;
    }

    public void setFlag(Flag flag) {
        mFlag = flag;
    }

    public boolean isBought() {
        return mIsBought;
    }

    public void setBought(boolean bought) {
        mIsBought = bought;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
