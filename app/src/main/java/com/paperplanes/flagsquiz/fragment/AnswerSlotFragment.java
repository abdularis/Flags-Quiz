package com.paperplanes.flagsquiz.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paperplanes.flagsquiz.R;
import com.paperplanes.flagsquiz.core.KeyboardArray;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by abdularis on 01/05/17.
 */

public class AnswerSlotFragment extends Fragment {

    private OnSlotClickListener mSlotClickCallback;
    private OnSlotFullListener mSlotFullCalback;
    private RelativeLayout mLayout;
    private ArrayList<Button> mSlots;
    private int mCurrSlotIdx;
    private View.OnClickListener mButtonOnClick;
    private boolean mEnableClick;

    public AnswerSlotFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_answer_slot, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayout = (RelativeLayout) view.findViewById(R.id.layout_slots);
        mSlots = new ArrayList<>();
        mCurrSlotIdx = 0;
        mEnableClick = true;
        mButtonOnClick = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!mEnableClick) return;
                Button btn = (Button) v;
                btn.setText("");
                int idx = mSlots.indexOf(v);
                if (idx < mCurrSlotIdx) mCurrSlotIdx = idx;

                if (mSlotClickCallback != null) {
                    KeyboardArray.Key key = (KeyboardArray.Key) v.getTag();
                    mSlotClickCallback.onSlotClick(key);
                    v.setTag(null);
                }
            }
        };
    }

    public ArrayList<KeyboardArray.Key> clearSlot() {
        ArrayList<KeyboardArray.Key> keys = new ArrayList<>();
        for (Button button : mSlots) {
            KeyboardArray.Key key = (KeyboardArray.Key) button.getTag();
            if (key != null) {
                keys.add(key);
                button.setTag(null);
            }

            button.setText("");
        }
        mCurrSlotIdx = 0;

        return keys;
    }

    public void setStringToSlots(@NonNull String str) {
        str = str.toUpperCase().replaceAll(" ", "").trim();
        for (int i = 0; i < mSlots.size() && i < str.length(); i++) {
            mSlots.get(i).setText(String.valueOf(str.charAt(i)));
        }
        mCurrSlotIdx = mSlots.size();
    }

    public void addKeyToSlot(KeyboardArray.Key key) {
        Button btn = getCurrentSlot();
        if (btn != null) {
            btn.setText(String.valueOf(key.getChar()));
            btn.setTag(key);

            Animator anim = AnimatorInflater.loadAnimator(getActivity(), R.animator.fade_in_out);
            anim.setTarget(btn);
            anim.start();

            while (true) {
                mCurrSlotIdx++;
                boolean last = mCurrSlotIdx >= mSlots.size();
                if (last) {
                    if (mSlotFullCalback != null) mSlotFullCalback.onFull(getStringFromSlots());
                    break;
                }
                else if (mSlots.get(mCurrSlotIdx).getText().length() <= 0) {
                    break;
                }
            }
        }
    }

    private Button getCurrentSlot() {
        if (mCurrSlotIdx >= mSlots.size() || mSlots.size() <= 0) return null;
        return mSlots.get(mCurrSlotIdx);
    }

    public boolean isSlotFull() {
        return mCurrSlotIdx >= mSlots.size();
    }

    public void setSlotLayoutFromString(@NonNull String str) {
        if (!str.isEmpty()) {
            mCurrSlotIdx = 0;
            mSlots.clear();
            mLayout.removeAllViews();

            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int wordVertMargin = getResources().getDimensionPixelSize(R.dimen.answer_slot_top_margin);
            int splitWordVertMargin = Math.max(0, wordVertMargin * 50 / 100);
            int wordSpace = getResources().getDimensionPixelOffset(R.dimen.answer_slot_space);
            int screenWidth = metrics.widthPixels;
            int slotSize = getResources().getDimensionPixelSize(R.dimen.answer_slot_size);
            int slotSpacing = getResources().getDimensionPixelOffset(R.dimen.common_padding_0);
            int currWidth = 0;
            boolean splitWord = false;

            ArrayList<String> words = new ArrayList<>(Arrays.asList(str.split(" ")));
            LinearLayout lastLayout = null;
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);

                LinearLayout linearLayout = null;
                RelativeLayout.LayoutParams lParams =
                        new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                if (lastLayout != null) {
                    int futureWidth = (word.length() * slotSize) + (slotSpacing * word.length()) + currWidth;

                    if (currWidth >= screenWidth || futureWidth >= screenWidth || splitWord) {
                        lParams.addRule(RelativeLayout.BELOW, lastLayout.getId());
                        if (splitWord) {
                            TextView hypen = new TextView(getActivity());
                            hypen.setText("-");
                            hypen.setTextColor(getResources().getColor(android.R.color.white));
                            hypen.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                                    getResources().getDimensionPixelSize(R.dimen.answer_slot_text));
                            hypen.setTextScaleX(2.0f);
                            LinearLayout.LayoutParams tvParams = new
                                    LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            tvParams.setMargins(10, 0, 0, 0);
                            lastLayout.addView(hypen, tvParams);
                            splitWord = false;
                        }
                        lParams.setMargins(0, wordVertMargin, 0, 0);
                        currWidth = 0;
                    } else {
                        linearLayout = lastLayout;
                        View v = linearLayout.getChildAt(linearLayout.getChildCount() - 1);
                        LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) v.getLayoutParams();
                        p.setMargins(0, 0, wordSpace, 0);
                    }

                }

                if (linearLayout == null) {
                    linearLayout = new LinearLayout(getActivity());
                    mLayout.addView(linearLayout, lParams);
                    linearLayout.setId(Math.abs((int) (Math.random() * 1000)));
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
                }

                int slotsWidth = (word.length() * slotSize) + (slotSpacing * word.length());
                if (slotsWidth > (screenWidth - slotSize)) {
                    int wLen = word.length();
                    String nextWord = word.substring(Math.min(7, wLen));
                    word = word.substring(0, Math.min(7, wLen));

                    words.add(i+1, nextWord);
                    splitWord = true;
                }
                ArrayList<Button> buttons = createSlotButtons(linearLayout, slotSize, slotSpacing, word.length());
                mSlots.addAll(buttons);

                currWidth += slotsWidth;
                lastLayout = linearLayout;
            }
        }
    }

    private ArrayList<Button> createSlotButtons(ViewGroup parentLayout, int btnSize, int btnSpacing, int count) {
        ArrayList<Button> buttons = new ArrayList<>();
        for (int j = 0; j < count; j++) {
            LinearLayout.LayoutParams p =
                    new LinearLayout.LayoutParams(btnSize, btnSize);
            p.setMargins(0, 0, getResources().getDimensionPixelOffset(R.dimen.common_padding_0), 0);

            Button button = new Button(getActivity());
            button.setLayoutParams(p);
            button.setTypeface(null, Typeface.BOLD);
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getResources().getDimension(R.dimen.answer_slot_text));
            button.setTextColor(getResources().getColor(android.R.color.white));
            button.setBackgroundResource(R.drawable.selector_bg_orange);
            button.setOnClickListener(mButtonOnClick);

            parentLayout.addView(button);
            buttons.add(button);
        }
        return buttons;
    }

    private String getStringFromSlots() {
        String str = "";
        for (Button button : mSlots) {
            str += button.getText();
        }
        return str.trim();
    }

    public void enableClick(boolean enable) {
        mEnableClick = enable;
    }

    public boolean isEnableClick() {
        return mEnableClick;
    }

    public void setOnSlotFullListener(OnSlotFullListener listener) {
        mSlotFullCalback = listener;
    }

    public void setOnSlotClickListener(OnSlotClickListener listener) {
        mSlotClickCallback = listener;
    }


    public interface OnSlotClickListener {
        void onSlotClick(KeyboardArray.Key key);
    }

    public interface OnSlotFullListener {
        void onFull(String slotsString);
    }
}
