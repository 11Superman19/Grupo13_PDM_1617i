package com.example.pedrofialho.myweatherapp.presentation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import com.example.pedrofialho.myweatherapp.presentation.widget.PackShotView
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class DetailForecastActivity : AppCompatActivity() {
    private lateinit var forecast_detail : WeatherForecast.List_Weather
    /**
     * @property actionBarId the identifier of the toolbar as specified in the activity layout, or
     * null if the activity does not include a toolbar
     */
    val actionBarId: Int? = R.id.toolbar

    /**
     * @property actionBarMenuResId the menu resource identifier that specifies the toolbar's
     * contents, or null if the activity does not include a toolbar
     */
    val actionBarMenuResId: Int? = R.menu.action_bar_activity

    lateinit var mToolbar : Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_forecast)
        val it = intent
        forecast_detail = it.getParcelableExtra<WeatherForecast.List_Weather>("DTO_Details")
        mToolbar = findViewById(R.id.toolbar) as Toolbar
        mToolbar.title = forecast_detail.city?.name

        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        mToolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(0,R.anim.slide_right)
        }



        (application as WeatherApplication).let {
            (findViewById(R.id.packShot) as PackShotView).setWeatherInfo(forecast_detail, it.imageLoader, buildConfigUrl())
        }

        (findViewById(R.id.temp) as TextView).text = ""+(forecast_detail.temp.day-273.15).toInt()+"ºC"
        (findViewById(R.id.weather) as TextView).text = forecast_detail.weather[0].main
        (findViewById(R.id.day) as TextView).text = resources.getString(R.string.day_detail)+" "+getDay(forecast_detail.dt)
        (findViewById(R.id.humidity) as TextView).text = resources.getString(R.string.humidity_detail)+" "+forecast_detail.humidity+"%"
        (findViewById(R.id.pressure) as TextView).text = resources.getString(R.string.pressure_detail)+" "+forecast_detail.pressure+" hPa"
        (findViewById(R.id.Wind_speed) as TextView).text = resources.getString(R.string.wind_speed_detail)+" "+forecast_detail.speed+"m/s"
        (findViewById(R.id.tempmax) as TextView).text = resources.getString(R.string.temp_max)+" "+(forecast_detail.temp.max-273.15).toInt()+"ºC"
        (findViewById(R.id.tempmin) as TextView).text = resources.getString(R.string.temp_min)+" "+(forecast_detail.temp.min-273.15).toInt()+"ºC"
        (findViewById(R.id.clouds) as TextView).text = resources.getString(R.string.clouds_detail)+" "+forecast_detail.clouds+"%"
        (findViewById(R.id.rain) as TextView).text = resources.getString(R.string.rain_detail)+" "+forecast_detail.rain
        (findViewById(R.id.snow) as TextView).text = resources.getString(R.string.snow_detail)+" "+forecast_detail.snow


    }

    private fun getDay(dt:Long): String {
        val date = Date(dt * 1000L) // *1000 is to convert seconds to milliseconds
        val to_return : String?

        val sdf = SimpleDateFormat("dd-MM-yyyy")

        sdf.timeZone = TimeZone.getTimeZone("UTC+0")
        val formattedDate = sdf.format(date)
        to_return = formattedDate
        return to_return
    }

    private fun buildConfigUrl(): String {
        val baseUrl : String = "http://openweathermap.org/img/w/"
        val endUrl : String = ".png"
        return "$baseUrl${forecast_detail.weather[0].icon}$endUrl"
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        actionBarMenuResId?.let {
            menuInflater.inflate(it, menu)
            return true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.action_credits -> {
            startActivity(Intent(this, CreditsActivity::class.java))
            true
        }
        R.id.action_settings -> {
            startActivity(Intent(this,SettingsActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
