package com.example.pedrofialho.myweatherapp.services

import android.app.IntentService
import android.content.Intent
import android.support.annotation.WorkerThread
import com.android.volley.toolbox.RequestFuture
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.comms.GetRequest
import com.example.pedrofialho.myweatherapp.model.WeatherForecast


class WeatherCityUpdater() : IntentService("WeatherCityUpdater"){
    var city : String = "Lisbon"

    val PREFS_NAME = "MyPrefsFile"

    override fun onCreate() {
        super.onCreate()
        // Restore preferences
        val settings = getSharedPreferences(PREFS_NAME, 0)
        val silent = settings.getString("city", city)
        city = silent
        val editor = settings.edit()
        editor.clear()
        editor.apply()
    }

    private fun fetchWeatherForecastSync() : WeatherForecast{

        val future : RequestFuture<WeatherForecast> = RequestFuture.newFuture()
        (application as WeatherApplication).requestQueue.add(GetRequest<WeatherForecast>(
            buildConfigUrlForecast(),WeatherForecast::class.java,
                {response -> future.onResponse(response)},
                {error -> future.onErrorResponse(error)}
        ))
        return future.get()
    }

    private fun buildConfigUrlForecast(): String {
        val baseUrl = resources.getString(R.string.api_base_url_forecast)
        val api_count = resources.getString(R.string.api_count)
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$baseUrl$city&$api_count&$api_key"
    }

    @WorkerThread
    override fun onHandleIntent(intent: Intent?) {

        try {
           val list = fetchWeatherForecastSync()
            // TODO : Use list data
        }catch (error : Exception){
            // TODO : Handle error
        }
    }

}
