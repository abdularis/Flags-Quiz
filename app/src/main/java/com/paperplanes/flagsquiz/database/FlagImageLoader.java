package com.paperplanes.flagsquiz.database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;

import com.paperplanes.flagsquiz.R;
import com.paperplanes.flagsquiz.domain.Flag;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by abdularis on 04/05/17.
 */

public class FlagImageLoader {
    private static final String TAG = FlagImageLoader.class.getSimpleName();
    private static final String ASSET_THUMB_IMAGE_DIR = "images/thumb/";
    private static final String ASSET_IMAGE_DIR = "images/";

    private HashMap<Flag, Drawable> mImageCache;
    private HashMap<Flag, Drawable> mImageThumbCache;
    private Context mContext;

    public FlagImageLoader(Context context) {
        mImageCache = new HashMap<>();
        mImageThumbCache = new HashMap<>();
        mContext = context;
    }

    public Drawable getImage(Flag flag) {
        Drawable drawable = mImageCache.get(flag);
        if (drawable != null) return drawable;

        Bitmap bmp = getBitmapFromAsset(flag, ASSET_IMAGE_DIR);
        if (bmp != null) {
            int containerWidth =
                    mContext.getResources().getDimensionPixelOffset(R.dimen.flag_image_width);
            if (containerWidth < bmp.getWidth()) bmp = scaleBitmapKeepAspectRatio(bmp, containerWidth);

            drawable = new BitmapDrawable(mContext.getResources(), bmp);
            mImageCache.put(flag, drawable);
        }
        return drawable;
    }

    public Drawable getImageThumbnail(Flag flag) {
        Drawable drawable = mImageThumbCache.get(flag);
        if (drawable != null) return drawable;

        Bitmap bmp = getBitmapFromAsset(flag, ASSET_THUMB_IMAGE_DIR);
        if (bmp != null) {
            int scaledWidth =
                    mContext.getResources().getDimensionPixelOffset(R.dimen.flag_grid_view_width);
            drawable = new BitmapDrawable(mContext.getResources(),
                    scaleBitmapKeepAspectRatio(bmp, scaledWidth));
            mImageThumbCache.put(flag, drawable);
        }
        return drawable;
    }

    @Nullable
    private Bitmap getBitmapFromAsset(Flag flag, String assetDir) {
        String filename = assetDir + flag.getFlagDetail().getFlagAssetPath();
        try {
            return BitmapFactory.decodeStream(mContext.getAssets().open(filename));
        } catch (IOException e) {
            Log.d(TAG, "Failed to open stream from assets: " + filename);
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap scaleBitmapKeepAspectRatio(Bitmap src, int width) {
        int scaledHeight =
                (int) (((float) src.getHeight() / (float) src.getWidth()) * width);
        return Bitmap.createScaledBitmap(src, width, scaledHeight, true);
    }

}
