package com.paperplanes.flagsquiz.domain;

/**
 * Created by abdularis on 01/05/17.
 */

public class FlagDetail {

    private String mCountry;
    private String mCapital;
    private String mContinent;
    private String mFlagAssetPath;

    public FlagDetail() {
        this("", "", "", "");
    }

    public FlagDetail(String country, String flagAssetPath, String capital, String continent) {
        mCountry = country;
        mCapital = capital;
        mContinent = continent;
        mFlagAssetPath = flagAssetPath;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getCapital() {
        return mCapital;
    }

    public void setCapital(String capital) {
        mCapital = capital;
    }

    public String getContinent() {
        return mContinent;
    }

    public void setContinent(String continent) {
        mContinent = continent;
    }

    public String getFlagAssetPath() {
        return mFlagAssetPath;
    }

    public void setFlagAssetPath(String flagAssetPath) {
        mFlagAssetPath = flagAssetPath;
    }
}