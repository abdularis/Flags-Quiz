package com.paperplanes.flagsquiz.database;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.SparseArray;

import com.paperplanes.flagsquiz.domain.ContinentHint;
import com.paperplanes.flagsquiz.domain.Flag;
import com.paperplanes.flagsquiz.domain.FlagDetail;
import com.paperplanes.flagsquiz.domain.FullAnswerHint;
import com.paperplanes.flagsquiz.domain.HalfAnswerHint;
import com.paperplanes.flagsquiz.domain.Hint;
import com.paperplanes.flagsquiz.domain.Level;
import com.paperplanes.flagsquiz.domain.LevelStat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abdularis on 04/05/17.
 */

public class FlagRepository {
    private static final String ASSET_LEVEL_DESCRIPTOR_FILE = "level_desc.xml";

    private AssetManager mAssetManager;
    private FlagPreference mFlagPreference;
    private HashMap<String, ArrayList<Flag>> mFlags;
    private ArrayList<Level> mLevels;

    public FlagRepository(Context context) {
        mAssetManager = context.getAssets();
        mFlagPreference = new FlagPreference(context);
        mFlags = new HashMap<>();
        mLevels = null;
    }

    public ArrayList<Level> getLevels() {
        if (mLevels == null) {
            try {
                InputStream is = mAssetManager.open(ASSET_LEVEL_DESCRIPTOR_FILE);
                mLevels = SAXFlagGameParser.getLevels(is);

                int answeredFlags = mFlagPreference.getAnsweredCount();
                for (Level level : mLevels) {
                    int count = getFlagsCount(level);
                    int answeredCount = getAnsweredFlagsCount(level);
                    level.getLevelStat().setAnsweredFlagsCount(answeredCount);
                    level.getLevelStat().setFlagsCount(count);
                }

//                checkLevelForUnlock();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mLevels;
    }

    public ArrayList<Flag> getFlags(Level level) {
        ArrayList<Flag> flags = mFlags.get(level.getName());

        if (flags == null) {
            try {
                InputStream is = mAssetManager.open(level.getLevelFile());
                ArrayList<FlagDetail> flagDetails = SAXFlagGameParser.getFlagDetails(is);
                flags = new ArrayList<>();
                for (FlagDetail flagDetail : flagDetails) {
                    Flag flag = new Flag(flagDetail, false);
                    boolean answered = mFlagPreference.isFlagAnswered(flag);
                    flag.setAnswered(answered);

                    flags.add(flag);
                }

                mFlags.put(level.getName(), flags);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return flags;
    }

    public Flag getFlag(Level level, int index) {
        ArrayList<Flag> flags = getFlags(level);
        if (flags != null && index >= 0 && index < flags.size()) return flags.get(index);
        return null;
    }

    public int getFlagsCount() {
        int count = 0;
        for (Level l : mLevels) {
            count += getFlagsCount(l);
        }
        return count;
    }

    public int getFlagsCount(Level level) {
        if (level != null) {
            ArrayList<Flag> flags = getFlags(level);
            if (flags != null) return flags.size();
        }
        return 0;
    }

    public int getAnsweredFlagsCount() {
        return mFlagPreference.getAnsweredCount();
    }

    public int getAnsweredFlagsCount(Level level) {
        int count = 0;
        ArrayList<Flag> flags = getFlags(level);
        if (flags != null) {
            for (Flag flag : flags) {
                if (flag.isAnswered()) count++;
            }
        }

        return count;
    }

    public ArrayList<Flag> getAnsweredFlags() {
        ArrayList<Flag> answeredFlags = new ArrayList<>();
        for (String key : mFlags.keySet()) {
            ArrayList<Flag> flags = mFlags.get(key);
            for (Flag flag : flags) {
                if (flag.isAnswered()) answeredFlags.add(flag);
            }
        }
        return answeredFlags;
    }

    public void setFlagAsAnswered(Flag flag) {
        mFlagPreference.markFlagAsAnswered(flag);
        Level level = getLevel(flag);
        if (level != null && level.getLevelStat() != null) {
            level.getLevelStat().setAnsweredFlagsCount( level.getLevelStat().getAnsweredFlagsCount() + 1 );

//            checkLevelForUnlock();
        }
    }

    public Hint getHint(Flag flag, String hintName) {
        Hint hint = null;
        if (hintName.equalsIgnoreCase(ContinentHint.NAME)) hint = new ContinentHint(flag);
        else if (hintName.equalsIgnoreCase(HalfAnswerHint.NAME)) hint = new HalfAnswerHint(flag);
        else if (hintName.equalsIgnoreCase(FullAnswerHint.NAME)) hint = new FullAnswerHint(flag);

        if (mFlagPreference.isHintBought(hint)) hint.setBought(true);

        return hint;
    }

    public void markHintAsBought(Hint hint) {
        hint.setBought(true);
        mFlagPreference.markHintAsBought(hint);
    }

    public void clearAnsweredFlags() {
        mFlagPreference.clearAnsweredFlags(getAnsweredFlags());
    }

    private Level getLevel(Flag flag) {
        for (Level level : mLevels) {
            ArrayList<Flag> flags = getFlags(level);
            if (flags != null) {
                for (Flag f : flags) {
                    if (f == flag) return level;
                }
            }
        }
        return null;
    }

//    private void checkLevelForUnlock() {
//        int answeredFlags = mFlagPreference.getAnsweredCount();
//        for (Level level : mLevels) {
//            if (level.getMinFlagsToUnlock() <= answeredFlags) {
//                level.setUnlocked(true);
//            }
//        }
//    }
}
