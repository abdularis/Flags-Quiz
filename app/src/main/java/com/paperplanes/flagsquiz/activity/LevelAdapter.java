package com.paperplanes.flagsquiz.activity;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.paperplanes.flagsquiz.R;
import com.paperplanes.flagsquiz.domain.Level;
import com.paperplanes.flagsquiz.domain.LevelStat;

import java.util.ArrayList;

/**
 * Created by abdularis on 09/05/17.
 */

class LevelAdapter extends ArrayAdapter<Level> {

    private int mRes;
    private Context mContext;
    private ArrayList<Level> mLevels;

    LevelAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<Level> levels) {
        super(context, resource);
        mContext = context;
        mRes = resource;
        mLevels = levels;
        addAll(levels);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(mRes, parent, false);
            holder = new Holder();
            view.setTag(holder);
            holder.background = view.findViewById(R.id.level_item_background);
            holder.textLevelName = (TextView) view.findViewById(R.id.text_item_level_name);
            holder.textProgress = (TextView) view.findViewById(R.id.text_progress);
            holder.imageLock = view.findViewById(R.id.image_lock);
        }
        else {
            holder = (Holder) view.getTag();
        }

        Level level = mLevels.get(position);
        if (level != null) {
            LevelStat ls = level.getLevelStat();


            if (level.isUnlocked()) {
                holder.background.setBackgroundResource(R.drawable.selector_bg_teal);
                holder.textLevelName.setVisibility(View.VISIBLE);
                holder.textProgress.setVisibility(View.VISIBLE);
                holder.imageLock.setVisibility(View.GONE);

                holder.textLevelName.setText(level.getName());
                String progress = ls.getAnsweredFlagsCount() + " / " + ls.getFlagsCount();
                holder.textProgress.setText(progress);
            }
            else {
                holder.background.setBackgroundResource(R.drawable.bg_grey);
                holder.textLevelName.setVisibility(View.GONE);
                holder.textProgress.setVisibility(View.GONE);
                holder.imageLock.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    private class Holder {
        View background;
        TextView textLevelName;
        TextView textProgress;
        View imageLock;
    }
}
