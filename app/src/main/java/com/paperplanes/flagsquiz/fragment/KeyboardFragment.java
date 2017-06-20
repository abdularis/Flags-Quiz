package com.paperplanes.flagsquiz.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.paperplanes.flagsquiz.R;
import com.paperplanes.flagsquiz.core.KeyboardArray;
import com.paperplanes.flagsquiz.core.SoundManager;

import java.util.ArrayList;

/**
 * Created by abdularis on 01/05/17.
 */

public class KeyboardFragment extends Fragment {

    private KeyboardArray mKeyboardArray;
    private ArrayList<Button> mKeyButtons;
    private OnKeyClickListener mCallback;
    private SoundManager mSoundManager;
    private boolean mEnableClick;

    public KeyboardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_keyboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEnableClick = true;
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mEnableClick) return;
                int idx = mKeyButtons.indexOf(v);
                KeyboardArray.Key[] keys = mKeyboardArray.getKeys();
                if (idx >= 0 && idx < keys.length) {
                    if (mCallback != null) mCallback.onKeyClick(keys[idx]);
                }

                mSoundManager.playSound(SoundManager.SOUND_KEY_CLICK);
            }
        };

        mSoundManager = SoundManager.getInstance(getActivity());
        mKeyButtons = new ArrayList<>();
        try {
            LinearLayout layoutKey = (LinearLayout) view;
            int viewCount = layoutKey.getChildCount();

            for (int i = 0; i < viewCount; i++) {
                LinearLayout layout = (LinearLayout) layoutKey.getChildAt(i);
                for (int j = 0; j < layout.getChildCount(); j++) {
                    Button btn = (Button) layout.getChildAt(j);
                    btn.setOnClickListener(clickListener);
                    mKeyButtons.add(btn);
                }
            }
        } catch (ClassCastException ex) {
        }

        mKeyboardArray = new KeyboardArray(mKeyButtons.size());
    }

    public void setKeysString(@NonNull String str) {
        resetKeyButtons();
        mKeyboardArray.generateRandomForString(str);
        KeyboardArray.Key[] keys = mKeyboardArray.getKeys();
        for (KeyboardArray.Key key : keys) {
            Button button = getKeyButton(key);
            button.setText(String.valueOf(key.getChar()));
        }
    }

    public Button getKeyButton(KeyboardArray.Key key) {
        int idx = key.getIndex();
        if (idx >= 0 && idx < mKeyButtons.size()) return mKeyButtons.get(idx);
        return null;
    }

    public void hideKey(KeyboardArray.Key key) {
        final Button btnKey = getKeyButton(key);
        if (btnKey != null) {
            btnKey.setEnabled(false);
            Animator anim = AnimatorInflater.loadAnimator(getActivity(), R.animator.zoom_out_fade_out);
            anim.setTarget(btnKey);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    btnKey.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        }
    }

    public void showKey(KeyboardArray.Key key) {
        final Button btnKey = getKeyButton(key);
        if (btnKey != null) {
            btnKey.setEnabled(true);
            btnKey.setVisibility(View.VISIBLE);
            Animator anim = AnimatorInflater.loadAnimator(getActivity(), R.animator.zoom_in_fade_in);
            anim.setTarget(btnKey);
            anim.start();
        }
    }

    public void setOnKeyClickListener(OnKeyClickListener listener) {
        mCallback = listener;
    }

    public void enableClick(boolean enable) {
        mEnableClick = enable;
    }

    public boolean isEnableClick() {
        return mEnableClick;
    }

    public KeyboardArray.Key getKey(char c) {
        KeyboardArray.Key[] keys = mKeyboardArray.getKeys();
        for (KeyboardArray.Key key : keys) {
            if (key.getChar() == c) return key;
        }
        return null;
    }

    private void resetKeyButtons() {
        for (Button button : mKeyButtons) {
            button.setEnabled(true);
            button.setAlpha(1);
            button.setVisibility(View.VISIBLE);
            button.setScaleX(1);
            button.setScaleY(1);
        }
    }

    public interface OnKeyClickListener {
        void onKeyClick(KeyboardArray.Key key);
    }
}
