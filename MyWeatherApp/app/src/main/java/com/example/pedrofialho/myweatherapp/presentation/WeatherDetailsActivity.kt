package com.example.pedrofialho.myweatherapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.presentation.widget.PackShotView
import java.text.DateFormat
import java.util.*

@Suppress("DEPRECATION")
class WeatherDetailsActivity : AppCompatActivity() {
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

    lateinit var mToolbar: Toolbar

    lateinit var weather_details: WeatherDetails

    companion object {
        val EXTRA_DETAILS = "weather_details_extra"

    fun createIntent(origin: Context, weatherDetail: WeatherDetails) =
            Intent(origin, WeatherDetailsActivity::class.java).putExtra(EXTRA_DETAILS, weatherDetail)
}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)

        mToolbar = findViewById(R.id.toolbar) as Toolbar

        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }


        val it = intent
        weather_details = it.getParcelableExtra(EXTRA_DETAILS)

        (application as WeatherApplication).let {
            (findViewById(R.id.packShot) as PackShotView).setWeatherInfo(weather_details, it.imageLoader, buildConfigUrl())
        }

        mToolbar.setNavigationOnClickListener {
            finish()
            startActivity(Intent(this, ChoiceActivity::class.java))
            overridePendingTransition(0,R.anim.slide_right)
        }


        (findViewById(R.id.temp) as TextView).text = ""+(weather_details.main.temp-273.15).toInt()+"ºC"
        (findViewById(R.id.weather) as TextView).text = weather_details.weather[0].main
        (findViewById(R.id.day) as TextView).text = resources.getString(R.string.day_detail)+" "+getDay()
        (findViewById(R.id.timeOfDay) as TextView).text = resources.getString(R.string.time)+" "+getCurrentTime()
        (findViewById(R.id.humidity) as TextView).text = resources.getString(R.string.humidity_detail)+" "+weather_details.main.humidity+"%"
        (findViewById(R.id.pressure) as TextView).text = resources.getString(R.string.pressure_detail)+" "+weather_details.main.pressure+" hPa"
        (findViewById(R.id.Wind_speed) as TextView).text = resources.getString(R.string.wind_speed_detail)+" "+weather_details.wind.speed+"m/s"
        (findViewById(R.id.tempmax) as TextView).text = resources.getString(R.string.temp_max)+" "+(weather_details.main.temp_max-273.15).toInt()+"ºC"
        (findViewById(R.id.tempmin) as TextView).text = resources.getString(R.string.temp_min)+" "+(weather_details.main.temp_min-273.15).toInt()+"ºC"
        (findViewById(R.id.clouds) as TextView).text = resources.getString(R.string.clouds_detail)+" "+weather_details.clouds!!.all+"%"
        if(weather_details.rain==null)(findViewById(R.id.rain) as TextView).text = resources.getString(R.string.rain_detail)+" "+0
        else (findViewById(R.id.rain) as TextView).text = resources.getString(R.string.rain_detail)+" "+weather_details.rain

        if(weather_details.snow==null)  (findViewById(R.id.snow) as TextView).text = resources.getString(R.string.snow_detail)+" "+0
        else (findViewById(R.id.snow) as TextView).text = resources.getString(R.string.snow_detail)+" "+weather_details.snow


        val t = object : Thread() {

            override fun run() {
                try {
                    while (!isInterrupted) {
                        Thread.sleep(1000)
                        runOnUiThread { (findViewById(R.id.timeOfDay) as TextView).text = resources.getString(R.string.time)+" "+getCurrentTime() }
                    }
                } catch (e: InterruptedException) {
                }

            }
        }

        t.start()

    }


    private fun getCurrentTime() : String{
        val currentTimeString = DateFormat.getTimeInstance().format(Date())
        return currentTimeString
    }
    private fun getDay(): String {
        val currentDateString = DateFormat.getDateInstance().format(Date())
        return currentDateString
    }

    private fun buildConfigUrl(): String {
        val baseUrl : String = "http://openweathermap.org/img/w/"
        val endUrl : String = ".png"
        return "$baseUrl${weather_details.weather[0].icon}$endUrl"
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
