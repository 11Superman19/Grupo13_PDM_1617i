package com.example.pedrofialho.myweatherapp.presentation.widget

import android.content.Context
import android.graphics.Bitmap
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


class PackShotView(ctx: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(ctx, attrs, defStyle) {
    var bitmap : Bitmap? = null
    init {
        inflate(context, R.layout.pack_shot_view, this)
        (findViewById(R.id.packShotImage) as ImageView).setImageResource(R.drawable.pack_shot_empty)
        //(findViewById(R.id.packShotImage) as ImageView).setErrorImageResId(R.drawable.pack_shot_empty)

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
        bitmap = getBitmap(url,bitmap)
        (findViewById(R.id.packShotImage) as ImageView).setImageBitmap(bitmap)
    }

    fun getBitmap(key: String?,bitmap: Bitmap?) : Bitmap?{
        var newBitmap : Bitmap? = getBitmapFromMemCache(key)
        if(newBitmap == null){
            addBitmapToMemoryCache(key,bitmap)
        }
        newBitmap = getBitmapFromMemCache(key)
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
        bitmap = getBitmap(url,bitmap)
        (findViewById(R.id.packShotImage) as ImageView).setImageBitmap(bitmap)
    }
}
