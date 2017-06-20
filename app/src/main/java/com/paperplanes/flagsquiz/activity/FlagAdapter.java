package com.paperplanes.flagsquiz.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.paperplanes.flagsquiz.R;
import com.paperplanes.flagsquiz.database.FlagImageLoader;
import com.paperplanes.flagsquiz.domain.Flag;

import java.util.ArrayList;

/**
 * Created by abdularis on 05/05/17.
 */

public class FlagAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Flag> mFlags;
    private FlagImageLoader mFlagImageLoader;

    public FlagAdapter(LayoutInflater inflater,
                       ArrayList<Flag> flags, FlagImageLoader flagImageLoader) {
        mInflater = inflater;
        mFlags = flags;
        mFlagImageLoader = flagImageLoader;
        if (mFlags == null) mFlags = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mFlags.size();
    }

    @Override
    public Object getItem(int position) {
        return mFlags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = mInflater.inflate(R.layout.item_flag, parent, false);
            holder = new Holder();
            holder.imageFlag = (ImageView) v.findViewById(R.id.image_item_flag);
            holder.imageCheck = (ImageView) v.findViewById(R.id.image_item_check);
            v.setTag(holder);
        }
        else {
            holder = (Holder) v.getTag();
        }

        Flag flag = (Flag) getItem(position);
        holder.imageFlag.setImageDrawable(mFlagImageLoader.getImageThumbnail(flag));

        if (flag.isAnswered()) {
            holder.imageFlag.setColorFilter(Color.argb(150, 255, 255, 255));
            holder.imageCheck.setVisibility(View.VISIBLE);
        }
        else {
            holder.imageFlag.setColorFilter(Color.TRANSPARENT);
            holder.imageCheck.setVisibility(View.GONE);
        }

        return v;
    }

    private class Holder {
        ImageView imageFlag;
        ImageView imageCheck;
    }
}
