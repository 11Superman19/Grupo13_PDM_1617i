package com.example.pedrofialho.myweatherapp.services

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import com.android.volley.toolbox.ImageLoader
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import java.util.*

class LruImageCache : ImageLoader.ImageCache {
    private var map: LinkedHashMap<String, Bitmap>
    private var maxSize: Int = 0
    private var bitmap : Bitmap? = null

    private var createCount: Int = 0
    private var putCount : Int = 0
    private var hitCount: Int = 0
    private var missCount: Int = 0

    constructor(maxSize : Int){
        if (maxSize <= 0) {
            throw IllegalArgumentException("maxSize <= 0")
        }
        this.maxSize=maxSize
        this.map = LinkedHashMap<String,Bitmap>(0, 0.75f, true)
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
        create(key)
        val createdValue = bitmap ?: return null

        synchronized(this) {
            createCount++
            mapValue = map.put(key, createdValue)

            if (mapValue != null) {
                // There was a conflict so undo that last put
                map.put(key, mapValue as Bitmap)
            }
        }
       return createdValue
    }

    private fun create(key: String){
        getBitmapAsync(key)
    }

    private fun getBitmapAsync(url: String){
        (object : AsyncTask<String, Unit, Bitmap?>() {
            override fun doInBackground(vararg params: String?): Bitmap? {
                Log.v("Pedro", "doInBackground in ${Thread.currentThread().id}")
                var bis : BufferedInputStream? = null
                val out = ByteArrayOutputStream()
                try {
                    val mUrl = URL(url)
                    val inP = mUrl.openConnection().inputStream
                    bis = BufferedInputStream(inP,1024*8)
                    var len = 0
                    val buffer = ByteArray(1024)
                    do {
                        len = bis.read(buffer)
                        if(len==-1) break
                        out.write(buffer, 0, len)
                    } while (len != -1)
                    val data = out.toByteArray()
                    val image = BitmapFactory.decodeByteArray(data,0,data.size)
                    bitmap = image
                    return image
                }catch (ex : IOException){
                    ex.printStackTrace()
                    return null
                }
                    finally {
                        out.close()
                        bis?.close()
                    }
            }
            override fun onPostExecute(result: Bitmap?) {
                Log.v("Pedro","Sync done")
                if(result == null){
                    return
                }
                bitmap = result
            }
        }).execute().get()
    }

    override fun putBitmap(url: String?, bitmap: Bitmap?) {
        put(url,bitmap)
    }

    private fun put(url: String?, bitmap: Bitmap?) {
        var newBitmap : Bitmap? = null
        if (url == null) {
            throw NullPointerException("url == null")
        }
        if(bitmap == null){
            create(url)
            newBitmap = this.bitmap
        }
        if(newBitmap == null){
            return
        }
        synchronized(this) {
            putCount++
            map.put(url, newBitmap as Bitmap)
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