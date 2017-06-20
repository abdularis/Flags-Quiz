package com.paperplanes.flagsquiz.domain;

/**
 * Created by abdularis on 09/05/17.
 */

public class Level {

    private String mName;
    private String mLevelFile;
    private int mMinFlagsToUnlock;
    private boolean mIsUnlocked;
    private LevelStat mLevelStat;

    public Level() {
        this("", "", 0, new LevelStat());
    }

    public Level(String name, String levelFile, int minFlagsToUnlock, LevelStat levelStat) {
        mName = name;
        mLevelFile = levelFile;
        mMinFlagsToUnlock = minFlagsToUnlock;
        mLevelStat = levelStat;
        mIsUnlocked = false;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLevelFile() {
        return mLevelFile;
    }

    public void setLevelFile(String levelFile) {
        mLevelFile = levelFile;
    }

    public int getMinFlagsToUnlock() {
        return mMinFlagsToUnlock;
    }

    public void setMinFlagsToUnlock(int minFlagsToUnlock) {
        mMinFlagsToUnlock = minFlagsToUnlock;
    }

    public boolean isUnlocked() {
        return mIsUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        mIsUnlocked = unlocked;
    }

    public LevelStat getLevelStat() {
        return mLevelStat;
    }

    public void setLevelStat(LevelStat levelStat) {
        mLevelStat = levelStat;
    }
}
