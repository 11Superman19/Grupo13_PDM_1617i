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
import android.widget.Toast
import com.android.volley.toolbox.ImageLoader
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import java.io.*


class PackShotView(ctx: Context, attrs: AttributeSet?, defStyle: Int) : LinearLayout(ctx, attrs, defStyle) {
    var bitmap : Bitmap? = null
    lateinit var path : String
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
        val cw = ContextWrapper(this.context.applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        val myDir = File(directory, "Image.png")
        try {
            val f = File(myDir, "profile.jpg")
            val b = BitmapFactory.decodeStream(FileInputStream(f))
            return  b
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return null
    }

    private fun saveImage(bitmap: Bitmap?,key : String?) {
        val cw = ContextWrapper(this.context.applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val myDir = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val bool = myDir.mkdirs()
        if(bool) Log.v("Pedro","Created")
        // Create a name for the saved image
        val file = File(myDir, "Image.png")

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    fun getBitmap(url: String?, bitmap: Bitmap?,key: String?) : Bitmap?{
        var newBitmap : Bitmap? = getBitmapFromMemCache(url)
        if(newBitmap==null){
            newBitmap = getImage(" ")
        }
        if(newBitmap == null){
            addBitmapToMemoryCache(url,bitmap)
        }
       saveImage(newBitmap,key)
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
