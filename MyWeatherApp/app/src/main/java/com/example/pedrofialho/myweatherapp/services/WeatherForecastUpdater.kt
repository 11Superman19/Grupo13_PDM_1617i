package com.example.pedrofialho.myweatherapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.comms.GetRequest
import com.example.pedrofialho.myweatherapp.model.WeatherForecast


class WeatherForecastUpdater : Service() {


    companion object{
        val WEATHER_LIST_ID_EXTRA_KEY = "listID"
        val UPCOMING_LIST_ID_EXTRA_VALUE = "forecast/daily?q="
        val DAILY_ID_EXTRA_VALUE = "weather?q="

        private val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    var city : String = "Lisbon"

    val PREFS_NAME = "MyPrefsFile"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Restore preferences
        val settings = getSharedPreferences(PREFS_NAME, 0)
        val silent = settings.getString("city", city)
        city = silent
        val editor = settings.edit()
        editor.clear()
        editor.apply()
        (application as WeatherApplication).requestQueue.add(
                GetRequest<WeatherForecast>(
                        buildConfigUrlForecast(),
                        WeatherForecast::class.java,
                        {
                            Log.v("DEMO","Sucess")
                            (application as WeatherApplication).weatherForecast = it
                            stopSelf(startId)
                        },
                        {
                            Log.v("DEMO","Error")

                            //TODO : Handle error
                            stopSelf(startId)
                        }
                )
        )
        return Service.START_FLAG_REDELIVERY
    }
    private fun buildConfigUrlForecast(): String {
        val baseUrl = resources.getString(R.string.api_base_url_forecast)
        val api_count = resources.getString(R.string.api_count)
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$baseUrl$city&$api_count&$api_key"
    }

}