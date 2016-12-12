package com.example.pedrofialho.myweatherapp

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import com.example.pedrofialho.myweatherapp.services.NullImageCache
import com.example.pedrofialho.myweatherapp.services.WeatherForecastUpdater


class WeatherApplication : Application(){
    /**
     * @property weatherDetails The configuration information provided by the remote API,
     * or null if we could not reach it
     */

    var weatherDetails : WeatherDetails? = null
    /**
     * @property weatherDetails The configuration information provided by the remote API,
     * or null if we could not reach it
     */

    var weatherForecast: WeatherForecast? = null

    /**
     * @property requestQueue The request queue to be used for request dispatching
     */
    lateinit var requestQueue: RequestQueue

    /**
     * @property imageLoader The image loader instance, used to load images from the network
     */
    lateinit var imageLoader: ImageLoader

    /**
     * Initiates the application instance
     */
    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)
        imageLoader = ImageLoader(requestQueue, NullImageCache())

        val alarmManager_forecast = getSystemService(ALARM_SERVICE) as AlarmManager

        alarmManager_forecast.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                0,
                AlarmManager.INTERVAL_DAY,
                PendingIntent.getService(
                        this,
                        1,
                        Intent(this, WeatherForecastUpdater::class.java),
                        PendingIntent.FLAG_UPDATE_CURRENT
                )
        )
    }
    }