package com.paperplanes.flagsquiz.core;

import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.util.Random;

/**
 * Created by abdularis on 01/05/17.
 */

public class KeyboardArray {
    private static final int ALPHA_COUNT = 26;
    private static final int ASCII_ALPHA_OFFSET = 65;

    private Random mRand;
    private Key[] mKeys;

    public KeyboardArray(int keyCount) {
        mRand = new Random();
        mRand.setSeed(SystemClock.currentThreadTimeMillis());
        mKeys = new Key[keyCount];
        for (int i = 0; i < keyCount; i++) mKeys[i] = new Key('\0', i);
    }

    public void generateRandomForString(@NonNull String str) {
        str = str.trim().replaceAll(" ", "").toUpperCase();
        if (str.length() > mKeys.length) str = str.substring(0, mKeys.length);

        for (Key k : mKeys) k.setChar('\0');
        for (int i = 0; i < str.length();) {
            int keyIdx = Math.abs(mRand.nextInt()) % mKeys.length;
            Key k = mKeys[keyIdx];
            if (k.getChar() == '\0') {
                k.setChar(str.charAt(i));
                i++;
            }
        }

        for (Key k : mKeys) {
            if (k.getChar() == '\0') {
                char c = (char)((Math.abs(mRand.nextInt()) % ALPHA_COUNT) + ASCII_ALPHA_OFFSET);
                k.setChar(c);
            }
        }
    }

    public Key[] getKeys() {
        return mKeys;
    }

    public Key getKey(int index) {
        return mKeys[index];
    }

    public class Key {
        private char mChar;
        private int mIndex;

        public Key(char aChar, int index) {
            mChar = aChar;
            mIndex = index;
        }

        public char getChar() {
            return mChar;
        }

        public void setChar(char aChar) {
            mChar = aChar;
        }

        public int getIndex() {
            return mIndex;
        }
    }

}
