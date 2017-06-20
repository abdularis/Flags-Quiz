package com.paperplanes.flagsquiz.activity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paperplanes.flagsquiz.R;
import com.paperplanes.flagsquiz.core.FlagQuizGame;
import com.paperplanes.flagsquiz.core.KeyboardArray;
import com.paperplanes.flagsquiz.core.SoundManager;
import com.paperplanes.flagsquiz.domain.ContinentHint;
import com.paperplanes.flagsquiz.domain.Flag;
import com.paperplanes.flagsquiz.domain.FullAnswerHint;
import com.paperplanes.flagsquiz.domain.HalfAnswerHint;
import com.paperplanes.flagsquiz.domain.Level;
import com.paperplanes.flagsquiz.fragment.AnswerSlotFragment;
import com.paperplanes.flagsquiz.fragment.KeyboardFragment;

import java.util.ArrayList;

public class FlagQuizActivity extends FullscreenActivity
    implements KeyboardFragment.OnKeyClickListener, AnswerSlotFragment.OnSlotClickListener,
                AnswerSlotFragment.OnSlotFullListener, FlagQuizGame.OnCoinChangedListener,
                FlagQuizGame.OnLevelUnlockedListener {

    private FlagQuizGame mGame;
    private SoundManager mSoundManager;
    private ImageView mImageFlag;
    private ImageView mImageCheck;
    private View mClearBtn;
    private TextView mTextCoins;
    private View mLayoutCoins;
    private ImageButton mContinentHint;
    private ImageButton mHalfHint;
    private ImageButton mFullHint;
    private KeyboardFragment mKeyboardFragment;
    private AnswerSlotFragment mAnswerSlotFragment;

    private View mBtnPrevious;
    private View mBtnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flag_quiz);

        mGame = FlagQuizGame.getInstance(this);
        mGame.addOnLevelUnloackedListener(this);
        mGame.addOnCoinChangedListener(this);
        mSoundManager = SoundManager.getInstance(this);
        mClearBtn = findViewById(R.id.btn_clear);
        mImageFlag = (ImageView) findViewById(R.id.image_flag);
        mImageCheck = (ImageView) findViewById(R.id.image_check);
        mTextCoins = (TextView) findViewById(R.id.text_coins);
        mLayoutCoins = findViewById(R.id.layout_coins);
        mContinentHint = (ImageButton) findViewById(R.id.btn_continent_hint);
        mHalfHint = (ImageButton) findViewById(R.id.btn_half_answer_hint);
        mFullHint = (ImageButton) findViewById(R.id.btn_full_answer_hint);

        mTextCoins.setText(String.valueOf(mGame.getPreferences().getCoins()));

        FragmentManager fm = getSupportFragmentManager();
        mKeyboardFragment = (KeyboardFragment) fm.findFragmentById(R.id.fragment_keyboard);
        mAnswerSlotFragment = (AnswerSlotFragment) fm.findFragmentById(R.id.fragment_slots);
        mBtnNext = findViewById(R.id.btn_next);
        mBtnPrevious = findViewById(R.id.btn_previous);

        mKeyboardFragment.setOnKeyClickListener(this);
        mAnswerSlotFragment.setOnSlotClickListener(this);
        mAnswerSlotFragment.setOnSlotFullListener(this);

        setFlag(mGame.getCurrentFlag());
        updateFlagNavButton();
    }

    @Override
    public void onKeyClick(KeyboardArray.Key key) {
        if (!mAnswerSlotFragment.isSlotFull()) {
            mAnswerSlotFragment.addKeyToSlot(key);
            mKeyboardFragment.hideKey(key);
        }
    }

    @Override
    public void onSlotClick(KeyboardArray.Key key) {
        if (key != null) {
            mKeyboardFragment.showKey(key);
        }
    }

    @Override
    public void onFull(String slotsString) {
        if (mGame.answer(slotsString, mGame.getCurrentFlag())) {
            flagAnswered(mGame.getCurrentFlag());
            showToastMessage(true);
            mSoundManager.playSound(SoundManager.SOUND_CORRECT);
        }
        else {
            showToastMessage(false);
            mSoundManager.playSound(SoundManager.SOUND_WRONG);
        }
    }

    @Override
    public void onCoinChanged(int prevCoins, int currCoins) {
        mTextCoins.setText(String.valueOf(currCoins));
        Animator anim;
        if (currCoins > prevCoins) {
            anim = AnimatorInflater.loadAnimator(this, R.animator.zoom_in_out);
        }
        else {
            anim = AnimatorInflater.loadAnimator(this, R.animator.zoom_out_in);
        }

        anim.setTarget(mLayoutCoins);
        anim.start();
    }

    @Override
    public void levelUnlocked(Level level) {
//        Toast.makeText(this, level.getName() + " terbuka", Toast.LENGTH_SHORT).show();
    }

    private void showToastMessage(boolean isCorrect) {
        View toastView = getLayoutInflater().inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout));

        if (!isCorrect) {
            LinearLayout toastLayout = (LinearLayout) toastView.findViewById(R.id.toast_layout);
            TextView toastText = (TextView) toastView.findViewById(R.id.text_toast);
            ImageView toastImage = (ImageView) toastView.findViewById(R.id.image_toast);

            toastLayout.setBackgroundResource(R.drawable.bg_red);
            toastText.setText(getResources().getString(R.string.wrong));
            toastImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_clear));
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastView);
        toast.show();
    }

    public void setFlag(Flag flag) {
        if (flag != null) {
            mImageFlag.setImageDrawable(mGame.getFlagImageLoader().getImage(flag));
            mKeyboardFragment.setKeysString(flag.getFlagDetail().getCountry());
            mAnswerSlotFragment.setSlotLayoutFromString(flag.getFlagDetail().getCountry());
            if (flag.isAnswered()) {
                flagAnswered(flag);
            }
            else {
                flagUnAnswered(flag);
            }
        }
    }

    private void flagAnswered(Flag flag) {
        mAnswerSlotFragment.setStringToSlots(flag.getFlagDetail().getCountry());
        mAnswerSlotFragment.enableClick(false);
        mImageCheck.setVisibility(View.VISIBLE);
        mClearBtn.setVisibility(View.GONE);

        mContinentHint.setVisibility(View.GONE);
        mHalfHint.setVisibility(View.GONE);
        mFullHint.setVisibility(View.GONE);
    }

    private void flagUnAnswered(Flag flag) {
        mAnswerSlotFragment.enableClick(true);
        mImageCheck.setVisibility(View.GONE);
        mClearBtn.setVisibility(View.VISIBLE);

        mContinentHint.setVisibility(View.VISIBLE);
        mHalfHint.setVisibility(View.VISIBLE);
        mFullHint.setVisibility(View.VISIBLE);
    }

    public void onBtnPreviousClick(View view) {
        if (mGame.moveToPreviousFlag()) {
            setFlag(mGame.getCurrentFlag());
            updateFlagNavButton();
        }
    }

    public void onBtnNextClick(View view) {
        if (mGame.moveToNextFlag()) {
            setFlag(mGame.getCurrentFlag());
            updateFlagNavButton();
        }
    }

    public void onBtnClearClick(View view) {
        if (mAnswerSlotFragment.isEnableClick()) {
            ArrayList<KeyboardArray.Key> clearedKeys = mAnswerSlotFragment.clearSlot();
            for (KeyboardArray.Key k : clearedKeys) mKeyboardFragment.showKey(k);
        }
    }

    private void updateFlagNavButton() {
        if (mGame.hasNext()) mBtnNext.setVisibility(View.VISIBLE);
        else mBtnNext.setVisibility(View.INVISIBLE);

        if (mGame.hasPrevious()) mBtnPrevious.setVisibility(View.VISIBLE);
        else mBtnPrevious.setVisibility(View.INVISIBLE);
    }

    public void onBtnHintContinentClick(View view) {
        Flag flag = mGame.getCurrentFlag();
        if (flag != null) {
            ContinentHint hint = (ContinentHint) mGame.getFlagRepository().getHint(flag, ContinentHint.NAME);
            if (hint != null) {
                if (!hint.isBought()) {
                    if (!mGame.buyHint(hint)) return;
                }

                String continent = hint.getContinent();

                Toast.makeText(this, "Berada dibenua " + continent, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBtnHintHalfClick(View view) {
        Flag flag = mGame.getCurrentFlag();
        if (flag != null) {
            HalfAnswerHint hint = (HalfAnswerHint) mGame.getFlagRepository().getHint(flag, HalfAnswerHint.NAME);
            if (hint != null) {
                if (!hint.isBought()) {
                    if (!mGame.buyHint(hint)) return;
                }

                String half = hint.getHalfName().toUpperCase();

                for (int i = 0; i < half.length(); i++) {
                    KeyboardArray.Key key = mKeyboardFragment.getKey(half.charAt(i));
                    if (key != null) {
                        mKeyboardFragment.hideKey(key);
                        mAnswerSlotFragment.addKeyToSlot(key);
                    }
                }
            }

        }
    }


    public void onBtnHintFullClick(View view) {
        Flag flag = mGame.getCurrentFlag();
        if (flag != null) {
            FullAnswerHint hint = (FullAnswerHint) mGame.getFlagRepository().getHint(flag, FullAnswerHint.NAME);
            if (hint != null) {
                if (!hint.isBought()) {
                    if (!mGame.buyHint(hint)) return;
                }

                String half = hint.getFullAnswer().toUpperCase();

                for (int i = 0; i < half.length(); i++) {
                    KeyboardArray.Key key = mKeyboardFragment.getKey(half.charAt(i));
                    if (key != null) {
                        mKeyboardFragment.hideKey(key);
                        mAnswerSlotFragment.addKeyToSlot(key);
                    }
                }
            }

        }
    }

}
