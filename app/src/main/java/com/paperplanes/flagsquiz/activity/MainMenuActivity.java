package com.paperplanes.flagsquiz.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.paperplanes.flagsquiz.R;
import com.paperplanes.flagsquiz.core.GameSettings;

public class MainMenuActivity extends FullscreenActivity {

    private ImageButton mBtnSound;
    private GameSettings mGameSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mBtnSound = (ImageButton) findViewById(R.id.btn_enable_sound);
        mGameSettings = GameSettings.getInstance(this);

        updateBtnSound();
    }

    public void onPlayClick(View view) {
        Intent intent = new Intent(this, LevelSelectorActivity.class);
        startActivity(intent);
    }

    public void onBtnHelpClick(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    public void onBtnAboutClick(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void onBtnSoundClick(View view) {
        boolean isEnabled = mGameSettings.isSoundEnabled();
        mGameSettings.enableSound(!isEnabled);
        updateBtnSound();
    }

    private void updateBtnSound() {
        if (mGameSettings.isSoundEnabled()) {
            mBtnSound.setImageResource(R.drawable.ic_sound);
        }
        else {
            mBtnSound.setImageResource(R.drawable.ic_sound_off);
        }
    }
}
