package com.example.pedrofialho.myweatherapp.presentation.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.android.volley.toolbox.ImageLoader
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import java.io.File
import java.io.FileOutputStream
import java.util.*


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

    private fun getImage(key : String?) : Bitmap? {
        val root = this.context.filesDir.toString()
        val myDir = File(root + "/saved_images")
        val fname = "Image-$key.jpg"
        val file = File(myDir, fname)
        return BitmapFactory.decodeFile(file.toString())
    }

    private fun saveImage(key : String?) {
        val root = this.context.filesDir.toString()
        val myDir = File(root + "/saved_images")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$key.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
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
        if(newBitmap == null){
            Toast.makeText(this.context,"Image not available try connect to the web",Toast.LENGTH_SHORT).show()
        }
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
