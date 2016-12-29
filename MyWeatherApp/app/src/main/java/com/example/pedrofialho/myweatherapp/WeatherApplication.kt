package com.example.pedrofialho.myweatherapp

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.BatteryManager
import com.android.volley.RequestQueue
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.Volley
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import com.example.pedrofialho.myweatherapp.services.LruImageCache
import com.example.pedrofialho.myweatherapp.services.WeatherForecastUpdater


class WeatherApplication : Application(){

    /**
     * @property PREFS_NAME the name of the SharedPreferences to keep information
     */

    val PREFS_NAME = "MyPrefsFile"
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
     * @property mMemoryCache the cache for images
     */
    lateinit var mMemoryCache : LruImageCache
    /**
     * Initiates the application instance
     */
    override fun onCreate() {
        super.onCreate()
        requestQueue = Volley.newRequestQueue(this)
        mMemoryCache = LruImageCache(1024*8)
        imageLoader = ImageLoader(requestQueue, mMemoryCache)

        //Aqui meter o limite da bateria
        val batteryManager = (getSystemService(Context.BATTERY_SERVICE) as BatteryManager)
        val batLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val batteryString = getSharedPreferences(PREFS_NAME,0).getString("bateria","")
        var limiteBattery = 0
        if(batteryString==""){
            limiteBattery = 10
        }else{
            limiteBattery = Integer.parseInt(batteryString)
        }
        //Aqui fica o tip de dados do qual podemos fazer update
        val connManager = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val mInfoConn = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val minfoData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        val bothConn = getSharedPreferences(PREFS_NAME,0).getBoolean("connectivity_both",true)
        val typeInfoConn = getSharedPreferences(PREFS_NAME,0).getBoolean("connectivity",true) //assumir que esta no wifi

        fun scheduleUpdate(listId: String) {

            if (bothConn) {
                if(mInfoConn.isConnected) {
                    if (batLevel >= limiteBattery) {
                        sendIntent(listId)
                    }
                }else if (minfoData.isConnected){
                    sendIntent(listId)
                }
            }else if(mInfoConn.isConnected){
                    if(typeInfoConn){//true->wifi, false -> dados
                        sendIntent(listId)
                    }
            } else{
                if(!typeInfoConn){
                    sendIntent(listId)
                }
            }
        }
        // Implementation note: This solution does not persist Alarm schedules across reboots

        scheduleUpdate(WeatherForecastUpdater.UPCOMING_LIST_ID_EXTRA_VALUE)
        scheduleUpdate(WeatherForecastUpdater.DAILY_ID_EXTRA_VALUE)
    }
  private fun sendIntent(listId: String) {
        val action = Intent(this, WeatherForecastUpdater::class.java)
                .putExtra(WeatherForecastUpdater.WEATHER_LIST_ID_EXTRA_KEY, listId)

        (getSystemService(ALARM_SERVICE) as AlarmManager).setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                0,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                PendingIntent.getService(this, 1, action, PendingIntent.FLAG_UPDATE_CURRENT)
        )
    }
    }