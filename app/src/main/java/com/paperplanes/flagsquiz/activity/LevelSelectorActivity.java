package com.paperplanes.flagsquiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.paperplanes.flagsquiz.R;
import com.paperplanes.flagsquiz.core.FlagQuizGame;
import com.paperplanes.flagsquiz.domain.Level;

import java.util.ArrayList;

public class LevelSelectorActivity extends FullscreenActivity {

    private FlagQuizGame mGame;
    private LevelAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_selector);

        mGame = FlagQuizGame.getInstance(this);
        ListView listView = (ListView) findViewById(R.id.list_view_level);
        final ArrayList<Level> levels = mGame.getLevels();
        mAdapter = new LevelAdapter(this, R.layout.item_level, levels);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < levels.size()) {
                    Level level = levels.get(position);
                    if (level.isUnlocked()) {
                        goToLevel(level);
                    }
                    else {
                        int answeredFlags = mGame.getFlagRepository().getAnsweredFlagsCount();
                        int numFlagsToUnlock = level.getMinFlagsToUnlock() - answeredFlags;
                        String msg = "Jawab " + numFlagsToUnlock +
                                " bendera lagi untuk membuka " + level.getName();
                        Toast.makeText(LevelSelectorActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    private void goToLevel(Level level) {
        if (level != null) {
            mGame.setCurrentLevel(level);
            Intent intent = new Intent(this, FlagsActivity.class);
            startActivity(intent);
        }
    }
}
