package com.example.pedrofialho.myweatherapp.presentation

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.comms.GetRequest
import com.example.pedrofialho.myweatherapp.model.WeatherDetails

class ChoiceActivity : AppCompatActivity() {
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

    var animation_forecast : ObjectAnimator? = null

    var animation_daily : ObjectAnimator? = null

    var city : String = "Lisbon"

    val PREFS_NAME = "MyPrefsFile"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        // Restore preferences
        val settings = getSharedPreferences(PREFS_NAME, 0)
        val silent = settings.getString("city", city)
        city = silent
        val editor = settings.edit()
        editor.clear()
        editor.apply()

        animation_forecast = ObjectAnimator.ofFloat(findViewById(R.id.imageView11), "rotationY", 0.0f, 360f)
        animation_forecast!!.duration = 3600
        animation_forecast!!.repeatCount = ObjectAnimator.INFINITE
        animation_forecast!!.interpolator = AccelerateDecelerateInterpolator()
        animation_forecast!!.start()


        animation_daily = ObjectAnimator.ofFloat(findViewById(R.id.imageView8), "rotationY", 0.0f, 360f)
        animation_daily!!.duration = 3600
        animation_daily!!.repeatCount = ObjectAnimator.INFINITE
        animation_daily!!.interpolator = AccelerateDecelerateInterpolator()
        animation_daily!!.start()

        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
        }

        (findViewById(R.id.daily) as Button).setOnClickListener {
            fetchCityWeatherInfo()
            animation_daily!!.pause()
            animation_forecast!!.pause()
            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.VISIBLE
        }
        (findViewById(R.id.forecast) as Button).setOnClickListener {
            fetchWeatherForecastInfo()
            animation_daily!!.pause()
            animation_forecast!!.pause()
            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.VISIBLE
        }
    }

    private fun fetchWeatherForecastInfo() {
        startActivity(ForecastActivity.createIntent(this, (application as WeatherApplication).weatherForecast!!))
    }

    private fun fetchCityWeatherInfo() {

        (application as WeatherApplication).requestQueue.add(GetRequest<WeatherDetails>(
                buildConfigUrlDetails(),WeatherDetails::class.java,
                {weather -> startActivity(WeatherDetailsActivity.createIntent(this,weather)) },
                {handleFatalError()})
        )
    }

    private fun handleFatalError() {
            Toast.makeText(this, "Data Not Available", Toast.LENGTH_LONG).show()
            Handler(mainLooper).postDelayed( { finish() }, 3000)
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setMessage(resources.getString(R.string.leaving_option)).
                setPositiveButton(resources.getString(R.string.choice_yes))
                { dialog, which -> this@ChoiceActivity.finishAffinity() }.
                setNegativeButton(resources.getString(R.string.choice_no), null).setCancelable(false).
                show()
    }

    override fun onResume() {
        if(animation_forecast!!.isPaused) animation_forecast!!.start()
        if(animation_daily!!.isPaused) animation_daily!!.start()
        super.onResume()
    }

    private fun buildConfigUrlDetails(): String {
        val baseUrl = resources.getString(R.string.api_base_url)
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$baseUrl$city&$api_key"
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
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

}
