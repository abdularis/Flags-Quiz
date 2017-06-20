package com.paperplanes.flagsquiz.core;

import android.content.Context;

import com.paperplanes.flagsquiz.database.FlagImageLoader;
import com.paperplanes.flagsquiz.database.FlagRepository;
import com.paperplanes.flagsquiz.domain.Flag;
import com.paperplanes.flagsquiz.domain.Hint;
import com.paperplanes.flagsquiz.domain.Level;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by abdularis on 04/05/17.
 */

public class FlagQuizGame {
    private static final int COINS_INCREMENT = 100;

    private FlagRepository mFlagRepository;
    private FlagImageLoader mFlagImageLoader;
    private GameSettings mPreferences;

    private ArrayList<Flag> mCurrFlags;
    private Flag mCurrFlag;
    private int mLastCurrFlagIdx;
    private Level mCurrLevel;
    private int mCoins;

    private ArrayList<OnLevelUnlockedListener> mLevelUnlockedListeners;
    private ArrayList<OnCoinChangedListener> mCoinChangedListeners;

    private static FlagQuizGame sInstance;

    public static FlagQuizGame getInstance(Context context) {
        if (sInstance == null) sInstance = new FlagQuizGame(context);
        return sInstance;
    }

    private FlagQuizGame(Context context) {
        mFlagRepository = new FlagRepository(context);
        mFlagImageLoader = new FlagImageLoader(context);
        mPreferences = GameSettings.getInstance(context);
        mLastCurrFlagIdx = 0;

        mLevelUnlockedListeners = new ArrayList<>();
        mCoinChangedListeners = new ArrayList<>();

        checkUnlockLevel();
    }

    public FlagImageLoader getFlagImageLoader() {
        return mFlagImageLoader;
    }

    public GameSettings getPreferences() {
        return mPreferences;
    }

    public ArrayList<Flag> getCurrentFlags() {
        return mCurrFlags;
    }

    public Flag getCurrentFlag() {
        return mCurrFlag;
    }

    public boolean hasNext() {
        return mCurrFlag != null && mCurrFlags != null &&
                mLastCurrFlagIdx + 1 < mCurrFlags.size();
    }

    public boolean hasPrevious() {
        return mCurrFlag != null && mCurrFlags != null &&
                mLastCurrFlagIdx - 1 >= 0;
    }

    public boolean moveToNextFlag() {
        if (hasNext()) {
            mCurrFlag = mCurrFlags.get(++mLastCurrFlagIdx);
            return true;
        }
        return false;
    }

    public boolean moveToPreviousFlag() {
        if (hasPrevious()) {
            mCurrFlag = mCurrFlags.get(--mLastCurrFlagIdx);
            return true;
        }
        return false;
    }

    public ArrayList<Level> getLevels() { return mFlagRepository.getLevels(); }

    public Level getCurrentLevel() {
        return mCurrLevel;
    }

    public FlagRepository getFlagRepository() {
        return mFlagRepository;
    }

    public void setCurrentLevel(Level level) {
        if (level != mCurrLevel) {
            mCurrLevel = level;
            mCurrFlags = mFlagRepository.getFlags(mCurrLevel);
            if (mCurrFlags != null && mCurrFlags.size() > 0) mCurrFlag = mCurrFlags.get(0);
            mLastCurrFlagIdx = 0;
        }
    }

    public void setCurrentFlagIndex(int index) {
        if (mCurrFlags != null && index >= 0 && index < mCurrFlags.size()) {
            mCurrFlag = mCurrFlags.get(index);
            mLastCurrFlagIdx = index;
        }
    }

    public boolean answer(String playerInput, Flag flag) {
        playerInput = playerInput.replaceAll(" ", "");
        String flagCountry = flag.getFlagDetail().getCountry().replaceAll(" ", "");
        boolean correct = playerInput.equalsIgnoreCase(flagCountry);
        if (correct) {
            mFlagRepository.setFlagAsAnswered(flag);
            checkUnlockLevel();

            int prevCoins = mPreferences.getCoins();
            int newCoins = prevCoins + COINS_INCREMENT;
            mPreferences.setCoins(newCoins);
            callCoinChangedListener(prevCoins, newCoins);
        }

        return correct;
    }

    public boolean buyHint(Hint hint) {
        int coins = mPreferences.getCoins();
        if (coins - hint.getPrice() < 0) {
            return false;
        }

        mFlagRepository.markHintAsBought(hint);
        int newCoins = coins - hint.getPrice();
        mPreferences.setCoins(newCoins);
        callCoinChangedListener(coins, newCoins);

        return true;
    }

    public void addOnLevelUnloackedListener(OnLevelUnlockedListener listener) {
        mLevelUnlockedListeners.add(listener);
    }

    public void addOnCoinChangedListener(OnCoinChangedListener listener) {
        mCoinChangedListeners.add(listener);
    }

    public void resetCurrentState() {
        mCurrFlag = null;
        mCurrFlags = null;
        mLastCurrFlagIdx = 0;
        mCurrLevel = null;
    }

    public void resetGames() {
        mFlagRepository.clearAnsweredFlags();
        mPreferences.resetSettings();
    }

    private void checkUnlockLevel() {
        int answeredFlags = mFlagRepository.getAnsweredFlagsCount();
        ArrayList<Level> levels = mFlagRepository.getLevels();
        for (Level level : levels) {
            if (!level.isUnlocked() && answeredFlags >= level.getMinFlagsToUnlock()) {
                level.setUnlocked(true);
                callLevelUnlockedListener(level);
            }
        }
    }

    private void callLevelUnlockedListener(Level level) {
        for (OnLevelUnlockedListener listener : mLevelUnlockedListeners) {
            listener.levelUnlocked(level);
        }
    }

    private void callCoinChangedListener(int prevCoins, int currCoins) {
        for (OnCoinChangedListener listener : mCoinChangedListeners) {
            listener.onCoinChanged(prevCoins, currCoins);
        }
    }

    public interface OnLevelUnlockedListener {
        void levelUnlocked(Level level);
    }

    public interface OnCoinChangedListener {
        void onCoinChanged(int prevCoins, int currCoins);
    }
}
