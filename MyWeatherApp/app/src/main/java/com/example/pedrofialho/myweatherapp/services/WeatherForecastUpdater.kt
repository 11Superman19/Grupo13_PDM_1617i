package com.example.pedrofialho.myweatherapp.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.android.volley.VolleyError
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.comms.GetRequest
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import com.example.pedrofialho.myweatherapp.model.content.WeatherInfoProvider
import com.example.pedrofialho.myweatherapp.model.content.toContentValues


class WeatherForecastUpdater : Service() {


    companion object{
        val WEATHER_LIST_ID_EXTRA_KEY = "listID"
        val UPCOMING_LIST_ID_EXTRA_VALUE = "forecast/daily?q="
        val DAILY_ID_EXTRA_VALUE = "weather?q="

        private val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        private val LIST_IDS = listOf(UPCOMING_LIST_ID_EXTRA_VALUE, DAILY_ID_EXTRA_VALUE)
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

        val weatherlistId = intent?.let{
            val listID : String? = it.getStringExtra(WEATHER_LIST_ID_EXTRA_KEY)
            if(listID in LIST_IDS) listID else null
        }

        if(weatherlistId == null){
            stopSelf(startId)
            return Service.START_NOT_STICKY
        }
        if (weatherlistId == UPCOMING_LIST_ID_EXTRA_VALUE) {
            (application as WeatherApplication).requestQueue.add(
                    GetRequest<WeatherForecast>(
                            buildConfigUrlForecast(weatherlistId),
                            WeatherForecast::class.java,
                            {
                                Log.v("DEMO", "Sucess")
                                (application as WeatherApplication).weatherForecast = it
                                processWeatherForecast(it,weatherlistId)
                                stopSelf(startId)
                            },
                            {
                                Log.v("DEMO", "Error")
                                handleError(it)
                                stopSelf(startId)
                            }
                    )
            )
        } else{
            (application as WeatherApplication).requestQueue.add(
                    GetRequest<WeatherDetails>(
                    buildConfigUrlDetails(weatherlistId),
                            WeatherDetails::class.java,
                    {
                        Log.v("DEMO", "Sucess")
                        (application as WeatherApplication).weatherDetails = it
                        processWeatherDetails(it,weatherlistId)
                        stopSelf(startId)
                    },
                    {
                        Log.v("DEMO", "Error")
                        handleError(it)
                        stopSelf(startId)
                    })
            )
        }
        return Service.START_FLAG_REDELIVERY
    }

    private fun processWeatherForecast(forecast: WeatherForecast, weatherlistId: String) {
        // Implementation note: This solution removes all existing entries from the DB before
        // inserting the new ones. This is not the best approach.
        val tableUri =
                WeatherInfoProvider.FORECAST_CONTENT_URI

        contentResolver.delete(tableUri, null, null)
        //TODO : ACABAR O CONTENT VALUES
       val count = contentResolver.bulkInsert(tableUri, forecast.toContentValues())

        Log.v("DEMO", "Successfully updated $weatherlistId movie list with $count entries")
    }

    private fun processWeatherDetails(details: WeatherDetails, weatherlistId: String) {
        // Implementation note: This solution removes all existing entries from the DB before
        // inserting the new ones. This is not the best approach.
        val tableUri =
                WeatherInfoProvider.WEATHER_CONTENT_URI

        contentResolver.delete(tableUri, null, null)
        //TODO : ACABAR O CONTENT VALUES
        //val count = contentResolver.bulkInsert(tableUri, details.toContentValues())

        //Log.v("DEMO", "Successfully updated $weatherlistId movie list with $count entries")
    }

    private fun handleError(error: VolleyError) {
        // TODO
        Log.v("DEMO", "CABBUMMM")
    }

    private fun buildConfigUrlDetails(weatherListID: String): String {
            val baseUrl = resources.getString(R.string.api_base_url)
            val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
            return "$baseUrl$weatherListID$city&$api_key"
    }

    private fun buildConfigUrlForecast(weatherListID : String): String {
        val api_count = resources.getString(R.string.api_count)
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$BASE_URL$weatherListID$city&$api_count&$api_key"
    }

}
