package com.example.pedrofialho.myweatherapp.services

import android.graphics.Bitmap
import com.android.volley.toolbox.ImageLoader
import java.util.*

class LruImageCache : ImageLoader.ImageCache {
    var map: LinkedHashMap<String, Bitmap> = LinkedHashMap(0, 0.75f, true)//fazer como que esta variavel nao se inicie outra vez
    private var maxSize: Int = 0
    private var createCount: Int = 0
    private var putCount : Int = 0
    private var hitCount: Int = 0
    private var missCount: Int = 0

    constructor(maxSize : Int){
        if (maxSize <= 0) {
            throw IllegalArgumentException("maxSize <= 0")
        }
        this.maxSize=maxSize
    }

    override fun getBitmap(url: String?): Bitmap? {
        return get(url)
    }

    private fun get(key: String?): Bitmap? {
        if (key == null) {
            throw NullPointerException("key == null")
        }
        var mapValue: Bitmap?
        synchronized(this) {
            mapValue = map[key]
            if (mapValue != null) {
                hitCount++
                return mapValue as Bitmap
            }
            missCount++
        }
        return null
    }
    override fun putBitmap(url: String?, bitmap: Bitmap?) {
        put(url,bitmap)
    }

    private fun put(url: String?, bitmap: Bitmap?) {
        if (url == null) {
            throw NullPointerException("url == null")
        }
        synchronized(this) {
            putCount++
            map.put(url, bitmap as Bitmap)
        }

    }


    /**
     * For caches that do not override [.sizeOf], this returns the maximum
     * number of entries in the cache. For all other caches, this returns the
     * maximum sum of the sizes of the entries in this cache.
     */
    @Synchronized fun maxSize(): Int {
        return maxSize
    }

    /**
     * Returns the number of times [.get] returned a value that was
     * already present in the cache.
     */
    @Synchronized fun hitCount(): Int {
        return hitCount
    }

    /**
     * Returns the number of times [.get] returned null or required a new
     * value to be created.
     */
    @Synchronized fun missCount(): Int {
        return missCount
    }

    /**
     * Returns the number of times [.create] returned a value.
     */
    @Synchronized fun createCount(): Int {
        return createCount
    }

    /**
     * Returns the number of times [.put] was called.
     */
    @Synchronized fun putCount(): Int {
        return putCount
    }
    /**
     * Returns a copy of the current contents of the cache, ordered from least
     * recently accessed to most recently accessed.
     */
    @Synchronized fun snapshot(): Map<String, Bitmap> {
        return LinkedHashMap(map)
    }
}