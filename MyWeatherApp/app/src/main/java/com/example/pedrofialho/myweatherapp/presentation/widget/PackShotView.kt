package com.example.pedrofialho.myweatherapp.presentation.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
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
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.net.URL


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
        getBitmapAsync(url)
        bitmap = getBitmap(url,bitmap)
        (findViewById(R.id.packShotImage) as ImageView).setImageBitmap(bitmap)
    }

    private fun getBitmapAsync(url: String){
        (object : AsyncTask<String, Unit, Bitmap>() {
            override fun doInBackground(vararg params: String?): Bitmap {
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
                    this@PackShotView.bitmap = image
                    return image
                }finally {
                    out.close()
                    bis?.close()
                }
            }
            override fun onPostExecute(result: Bitmap?) {
                Log.v("Pedro","Sync done")
                bitmap = result
            }
        }).execute().get()
    }

    fun getBitmap(key: String?,bitmap: Bitmap?) : Bitmap?{
        addBitmapToMemoryCache(key,bitmap)
        val newBitmap : Bitmap? = getBitmapFromMemCache(key)
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
        getBitmapAsync(url)
        bitmap = getBitmap(url,bitmap)
        (findViewById(R.id.packShotImage) as ImageView).setImageBitmap(bitmap)
    }
}
