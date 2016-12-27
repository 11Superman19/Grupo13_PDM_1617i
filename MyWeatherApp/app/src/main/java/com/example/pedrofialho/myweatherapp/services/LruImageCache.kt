package com.example.pedrofialho.myweatherapp.services

import android.graphics.Bitmap
import android.util.LruCache
import com.android.volley.toolbox.ImageLoader

class LruImageCache(maxSize: Int) : LruCache<String, Bitmap>(maxSize), ImageLoader.ImageCache {

    companion object {
        fun getDefaultLruCacheSize(): Int {
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
            val cacheSize = maxMemory / 8

            return cacheSize
        }
    }


    override fun getBitmap(url: String?): Bitmap {
       return get(url)
    }

    override fun putBitmap(url: String?, bitmap: Bitmap?) {
       put(url,bitmap)
    }

    override fun sizeOf(key: String?, value: Bitmap?): Int = (value!!.byteCount)/1024
}