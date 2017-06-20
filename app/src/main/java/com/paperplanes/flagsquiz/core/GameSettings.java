package com.paperplanes.flagsquiz.core;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abdularis on 04/05/17.
 */

public class GameSettings {
    private static final String PREF_NAME = GameSettings.class.getSimpleName();
    private static final String PREF_ENABLE_SOUND = "sound.enable";
    private static final String PREF_COINS = "coins";

    private SharedPreferences mPref;
    private boolean mEnableSound;

    private static GameSettings sInstance;
    public static GameSettings getInstance(Context context) {
        if (sInstance == null) sInstance = new GameSettings(context);
        return sInstance;
    }

    private GameSettings(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEnableSound = mPref.getBoolean(PREF_ENABLE_SOUND, true);
    }

    public boolean isSoundEnabled() {
        return mEnableSound;
    }

    public void enableSound(boolean enable) {
        mEnableSound = enable;
        mPref.edit().putBoolean(PREF_ENABLE_SOUND, enable).apply();
    }

    public int getCoins() {
        return mPref.getInt(PREF_COINS, 0);
    }

    public void setCoins(int coins) {
        mPref.edit().putInt(PREF_COINS, coins).apply();
    }

    public void resetSettings() {
        mPref.edit().clear().apply();
    }
}
