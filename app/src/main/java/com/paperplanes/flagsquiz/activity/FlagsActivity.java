package com.paperplanes.flagsquiz.activity;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.paperplanes.flagsquiz.R;
import com.paperplanes.flagsquiz.core.FlagQuizGame;
import com.paperplanes.flagsquiz.domain.Level;
import com.paperplanes.flagsquiz.domain.LevelStat;

public class FlagsActivity extends FullscreenActivity {

    private FlagQuizGame mGame;
    private FlagAdapter mAdapter;
    private GridView mGridView;

    private TextView mTextLevel;
    private TextView mTextAnsweredFlagsCount;
    private TextView mTextFlagsCount;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flags);

        mGame = FlagQuizGame.getInstance(this);

        mAdapter = new FlagAdapter(
                (LayoutInflater)getSystemService(Service.LAYOUT_INFLATER_SERVICE),
                mGame.getCurrentFlags(), mGame.getFlagImageLoader());
        mGridView = (GridView) findViewById(R.id.grid_flags);
        mTextLevel = (TextView) findViewById(R.id.text_level_num);
        mTextAnsweredFlagsCount = (TextView) findViewById(R.id.text_answered_flags_count);
        mTextFlagsCount = (TextView) findViewById(R.id.text_flags_count);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_level);

        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mGame.setCurrentFlagIndex(position);
                goToQuizActivity();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_flags);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();

        Level currLevel = mGame.getCurrentLevel();
        if (currLevel != null) {
            LevelStat levStat = currLevel.getLevelStat();

            mTextLevel.setText(currLevel.getName());
            mTextAnsweredFlagsCount.setText(String.valueOf(levStat.getAnsweredFlagsCount()));
            mTextFlagsCount.setText(String.valueOf(levStat.getFlagsCount()));
            mProgressBar.setMax(levStat.getFlagsCount());
            mProgressBar.setProgress(levStat.getAnsweredFlagsCount());
        }
    }

    private void goToQuizActivity() {
        Intent intent = new Intent(this, FlagQuizActivity.class);
        startActivity(intent);
    }
}
