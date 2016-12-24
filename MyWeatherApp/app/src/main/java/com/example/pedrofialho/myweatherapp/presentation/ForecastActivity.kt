package com.example.pedrofialho.myweatherapp.presentation

import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.R.layout
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import com.example.pedrofialho.myweatherapp.model.content.WeatherInfoProvider
import com.example.pedrofialho.myweatherapp.model.content.toForecast

class ForecastActivity : ListActivity(){

    var weather_forecast : WeatherForecast? = null

    companion object{
        val EXTRA_FORECAST = "weather_forecast_extra"

        fun createIntent(origin: Context, weatherForecast: WeatherForecast?) =
                Intent(origin, ForecastActivity::class.java).putExtra(EXTRA_FORECAST, weatherForecast)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(layout.activity_weather)

        val it = intent
        weather_forecast = it.getParcelableExtra(EXTRA_FORECAST)

        if(weather_forecast == null){
            readValuesFromDataBase()
        }

       listView.adapter = ArrayAdapter<WeatherForecast.List_Weather>(
                this,
                android.R.layout.simple_list_item_1,
                weather_forecast?.list)
    }

    private fun readValuesFromDataBase() {
        val cursor : Cursor?
        val tableUri =
                WeatherInfoProvider.FORECAST_CONTENT_URI
        val projection = arrayOf(WeatherInfoProvider.COLUMN_ID,
                WeatherInfoProvider.COLUMN_HUMIDITY,
                WeatherInfoProvider.COLUMN_WEATHER_DESC,
                WeatherInfoProvider.COLUMN_PRESSURE,
                WeatherInfoProvider.COLUMN_TEMP,
                WeatherInfoProvider.COLUMN_TEMP_MAX,
                WeatherInfoProvider.COLUMN_TEMP_MIN,
                WeatherInfoProvider.COLUMN_CLOUDS,
                WeatherInfoProvider.COLUMN_RAIN,
                WeatherInfoProvider.COLUMN_CNT,
                WeatherInfoProvider.COLUMN_DT)
        cursor = contentResolver.query(tableUri,projection,null,null,null)
        cursor.moveToFirst()
        weather_forecast = toForecast(cursor = cursor)

    }


    override fun onListItemClick(l: ListView, v: View, position: Int, id: Long) {

        super.onListItemClick(l, v, position, id)

        // ListView Clicked item index
        val itemPosition = position
        val animation1 = AlphaAnimation(0.3f, 1.0f)
        animation1.duration = 200
        v.startAnimation(animation1)
        animation1.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                val intent = Intent(this@ForecastActivity, DetailForecastActivity::class.java)
                intent.putExtra("DTO_Details", weather_forecast?.list?.get(itemPosition))
                startActivityForResult(intent, 1)
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right)
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
    }
}
