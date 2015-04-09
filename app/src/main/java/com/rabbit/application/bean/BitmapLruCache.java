package com.rabbit.application.bean;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by Rabbit on 2015/3/23.
 */
public class BitmapLruCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {
    private static final String TAG = "BitmapLruCache";

    private BitmapSoftRefCache softRefCache;

    public BitmapLruCache(int maxSize) {
        super(maxSize);
        softRefCache = new BitmapSoftRefCache();
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
        if (evicted) {
            Log.i(TAG, "空间已满，缓存图片被挤出:" + key);
            // 将被挤出的bitmap对象，添加至软引用BitmapSoftRefCache
            softRefCache.putBitmap(key, oldValue);
        }
    }

    /**
     * 得到缓存对象
     */
    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap = get(url);
        // 如果bitmap为null，尝试从软引用缓存中查找
        if (bitmap == null) {
            bitmap = softRefCache.getBitmap(url);
        } else {
            Log.i(TAG, "LruCache：" + url);
        }
        return bitmap;
    }

    /**
     * 添加缓存对象
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }

}

