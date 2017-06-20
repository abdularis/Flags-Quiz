package com.paperplanes.flagsquiz.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.paperplanes.flagsquiz.domain.Flag;
import com.paperplanes.flagsquiz.domain.Hint;

import java.util.ArrayList;

/**
 * Created by abdularis on 04/05/17.
 */

class FlagPreference {
    private static final String PREF_NAME = FlagPreference.class.getSimpleName();
    private static final String PREF_ANSWERED_COUNT = "answered.count";

    private SharedPreferences mPref;
    private int mAnsweredCount;

    FlagPreference(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mAnsweredCount = mPref.getInt(PREF_ANSWERED_COUNT, 0);
    }

    int getAnsweredCount() {
        return mAnsweredCount;
    }

    boolean isFlagAnswered(Flag flag) {
        return flag != null && mPref.getBoolean(flag.getFlagDetail().getCountry(), false);
    }

    void markFlagAsAnswered(Flag flag) {
        if (flag != null) {
            mPref.edit()
                    .putBoolean(flag.getFlagDetail().getCountry(), true)
                    .putInt(PREF_ANSWERED_COUNT, ++mAnsweredCount)
                    .apply();

            flag.setAnswered(true);
        }
    }

    boolean isHintBought(Hint hint) {
        return hint != null && mPref.getBoolean(hint.getFlag().getFlagDetail().getCountry() + hint.getName(), false);
    }

    void markHintAsBought(Hint hint) {
        if (hint != null) {
            mPref.edit()
                    .putBoolean(hint.getFlag().getFlagDetail().getCountry() + hint.getName(), true)
                    .apply();
        }
    }

    void clearAnsweredFlags(ArrayList<Flag> flags) {
        SharedPreferences.Editor editor = mPref.edit();
        for (Flag flag : flags) {
            editor.remove(flag.getFlagDetail().getCountry());
            flag.setAnswered(false);
        }
        editor.putInt(PREF_ANSWERED_COUNT, 0);
        editor.apply();
        mAnsweredCount = 0;
    }
}
