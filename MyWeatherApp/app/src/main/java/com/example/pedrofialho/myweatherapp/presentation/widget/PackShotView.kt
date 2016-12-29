package com.example.pedrofialho.myweatherapp.presentation.widget

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.toolbox.ImageLoader
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import java.io.*


class PackShotView(ctx: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(ctx, attrs, defStyle) {
    var bitmap : Bitmap? = null
    init {
        inflate(context, R.layout.pack_shot_view, this)
        (findViewById(R.id.packShotImage) as ImageView).setImageResource(R.drawable.pack_shot_empty)

        if(isInEditMode)
            (findViewById(R.id.weatherTitle) as TextView).text = resources.getString(R.string.weather_details_tools_title)
    }

    constructor(ctx: Context) : this(ctx, null, 0)
    constructor(ctx: Context, attrs: AttributeSet) : this(ctx, attrs, 0)

    /**
     * Displays the pack shot for the given movie.
     * @param weatherDetail The movie information
     * @param imageLoader The pack shot image provider
     * @param urlBuilder The function used to convert the given image path to the corresponding URL
     */
    fun setWeatherInfo(weatherDetail: WeatherDetails?,
                     imageLoader: ImageLoader,
                     urlBuilder: String) {
        (findViewById(R.id.weatherTitle) as TextView).text = weatherDetail?.weather!![0].description
            val url = urlBuilder
            Log.v(resources.getString(R.string.app_name), "Displaying image from URL $url")
        bitmap = getBitmap(url,bitmap,weatherDetail?.weather[0].icon)
        if(bitmap == null){
            (findViewById(R.id.packShotImage) as ImageView).setImageResource(R.drawable.pack_shot_empty)
        }
        else (findViewById(R.id.packShotImage) as ImageView).setImageBitmap(bitmap)
    }

    private fun getImage(path : String?) : Bitmap? {
        try {
            val cw = ContextWrapper(this.context.applicationContext)
            // path to /data/data/yourapp/app_data/imageDir
            val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
            val f = File(directory.absolutePath, "profile.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            return b
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    private fun saveImage(key : String?) : String {
        val cw = ContextWrapper(this.context.applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, "profile.jpg")

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return directory.absolutePath
    }


    fun getBitmap(url: String?, bitmap: Bitmap?,key: String?) : Bitmap?{
        var newBitmap : Bitmap? = getBitmapFromMemCache(url)
        if(newBitmap==null){
            newBitmap = getImage(key)
        }
        if(newBitmap == null){
            addBitmapToMemoryCache(url,bitmap)
        }
        saveImage(key)
        newBitmap = getBitmapFromMemCache(url)
        return newBitmap
    }
    fun addBitmapToMemoryCache(key : String?, bitmap: Bitmap?){
            (this.context.applicationContext as WeatherApplication).mMemoryCache.putBitmap(key,bitmap)
    }

    fun getBitmapFromMemCache(key: String?): Bitmap? {
        return (this.context.applicationContext as WeatherApplication).mMemoryCache.getBitmap(key)
    }

    fun setWeatherInfo(weatherForecast: WeatherForecast.List_Weather,
                       imageLoader: ImageLoader,
                       urlBuilder: String) {

        (findViewById(R.id.weatherTitle) as TextView).text = weatherForecast.weather[0].description
        val url = urlBuilder
        Log.v(resources.getString(R.string.app_name), "Displaying image from URL $url")
        bitmap = getBitmap(url,bitmap,weatherForecast.weather[0].icon)
        if(bitmap == null){
            (findViewById(R.id.packShotImage) as ImageView).setImageResource(R.drawable.pack_shot_empty)
        }
        else (findViewById(R.id.packShotImage) as ImageView).setImageBitmap(bitmap)
    }
}
