package com.android.baselib.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.android.baselib.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * 图片加载维护(待完善)
 *
 * @author PF-NAN
 * @date 2018/11/9
 */
public class UImage {
    private static UImage mInstance = null;
    private WeakReference<Context> mWeakContext;

    private UImage() {
    }

    public static UImage getInstance() {
        if (null == mInstance) {
            synchronized (UImage.class) {
                if (null == mInstance) {
                    mInstance = new UImage();
                }
            }
        }
        return mInstance;
    }

    /**
     * 静态图加载
     *
     * @param context   上下文
     * @param url       url
     * @param defIcon   加载中、加载失败显示图片资源
     * @param imageView 图片控件
     */
    public void load(Context context, @NonNull String url, @DrawableRes int defIcon, ImageView imageView) {
        try {
            if (null != context && null != imageView) {
                mWeakContext = new WeakReference<>(context);
                RequestOptions options = new RequestOptions()
                        .placeholder(defIcon)
                        .error(defIcon)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                Glide.with(mWeakContext.get()).asBitmap().apply(options).load(url).into(imageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageResource(defIcon);
        }
    }

    /**
     * 静态图加载
     *
     * @param context   上下文
     * @param url       url
     * @param imageView 图片控件
     */
    public void load(Context context, @NonNull String url, ImageView imageView) {
        load(context,url,R.drawable.icon_defult_img,imageView);
    }


    /**
     * 加载本地图片
     *
     * @param context
     * @param filePath
     * @param defIcon
     * @param imageView
     */
    public void loadFile(Context context, @NonNull String filePath, @DrawableRes int defIcon, ImageView imageView) {
        try {
            if (null != context && null != imageView) {
                File file = new File(filePath);
                if (file.exists()) {
                    mWeakContext = new WeakReference<>(context);
                    RequestOptions options = new RequestOptions()
                            .placeholder(defIcon)
                            .error(defIcon)
                            .diskCacheStrategy(DiskCacheStrategy.NONE);
                    Glide.with(mWeakContext.get()).load(file)
                            .apply(options)
                            .into(imageView);
                } else {
                    imageView.setImageResource(defIcon);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageResource(defIcon);
        }

    }


    /**
     * Gif图加载
     *
     * @param context   上下文
     * @param url       url
     * @param defIcon   加载中、加载失败显示图片资源
     * @param imageView 图片控件
     */
    public void loadGif(@NonNull Context context, @NonNull String url, @DrawableRes int defIcon, @NonNull ImageView imageView) {
        try {
            mWeakContext = new WeakReference<>(context);
            RequestOptions options = new RequestOptions()
                    .override(Target.SIZE_ORIGINAL)
                    .placeholder(defIcon)
                    .error(defIcon)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
            Glide.with(mWeakContext.get()).asGif().apply(options).load(url).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
            imageView.setImageResource(defIcon);
        }
    }

    /**
     * @param context    上下文
     * @param url        url
     * @param startView  开始加载前显示控件
     * @param failedView 加载失败显示控件
     * @param imageView  图片控件
     */
    public void loading(@NonNull Context context, @NonNull String url, @NonNull final View startView, @NonNull final View failedView, @NonNull final ImageView imageView) {
        try {
            mWeakContext = new WeakReference<>(context);
            Glide.with(mWeakContext.get()).asBitmap().load(url).into(new ImageViewTarget<Bitmap>(imageView) {
                @Override
                public void onStart() {
                    super.onStart();
                    startView.setVisibility(View.VISIBLE);
                    failedView.setVisibility(View.INVISIBLE);
                }

                @Override
                protected void setResource(@Nullable Bitmap resource) {
                    startView.setVisibility(View.INVISIBLE);
                    if (null != resource) {
                        imageView.setImageBitmap(resource);
                        failedView.setVisibility(View.INVISIBLE);
                    } else {
                        failedView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    failedView.setVisibility(View.VISIBLE);
                    startView.setVisibility(View.INVISIBLE);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            startView.setVisibility(View.INVISIBLE);
            failedView.setVisibility(View.VISIBLE);
        }

    }
}
