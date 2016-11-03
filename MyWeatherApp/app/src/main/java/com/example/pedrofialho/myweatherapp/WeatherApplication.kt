package com.example.pedrofialho.myweatherapp

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import com.example.pedrofialho.myweatherapp.services.NullImageCache


class WeatherApplication : Application(){
    /**
     * @property weatherDetails The configuration information provided by the remote API,
     * or null if we could not reach it
     */
    var weatherDetails: WeatherDetails? = null


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
    }
}