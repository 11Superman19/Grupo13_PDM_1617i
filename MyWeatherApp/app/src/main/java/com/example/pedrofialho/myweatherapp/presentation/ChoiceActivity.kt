package com.example.pedrofialho.myweatherapp.presentation

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
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
import com.example.pedrofialho.myweatherapp.model.WeatherForecast

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
            navigateToDetails()
            animation_daily!!.pause()
            animation_forecast!!.pause()
            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.VISIBLE
        }
        (findViewById(R.id.forecast) as Button).setOnClickListener {
            navigateToForecast()
            animation_daily!!.pause()
            animation_forecast!!.pause()
            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.VISIBLE
        }
        Toast.makeText(this,"Fialho gay",Toast.LENGTH_LONG).show()
        Toast.makeText(this,"Tiago gay",Toast.LENGTH_LONG).show()
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

    private fun navigateToForecast() {
        (application as WeatherApplication).requestQueue.add(
                GetRequest(buildConfigUrlForecast(), WeatherForecast::class.java,
                        {
                            Log.v("Pedro","Success")
                            (application as WeatherApplication).weatherForecast = it
                            val intent = Intent(this, ForecastActivity::class.java)
                            intent.putExtra("DTO_forecast", (application as WeatherApplication).weatherForecast)
                            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.INVISIBLE
                            startActivity(intent)
                            overridePendingTransition( R.anim.slide_in_up, R.anim.stay)
                        },
                        {
                            Toast.makeText(this, "Data not available", Toast.LENGTH_LONG).show()
                            Handler(mainLooper).postDelayed({ finish() }, 3000)
                        }
                )
        )
    }

    private fun buildConfigUrlDetails(): String {
        val baseUrl = resources.getString(R.string.api_base_url)
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$baseUrl$city&$api_key"
    }

    private fun navigateToDetails() {
        (application as WeatherApplication).requestQueue.add(
                GetRequest(buildConfigUrlDetails(), WeatherDetails::class.java,
                        {
                            Log.v("Pedro","Success")
                            (application as WeatherApplication).weatherDetails = it
                            val intent = Intent(this,WeatherDetailsActivity::class.java)
                            intent.putExtra("DTO", (application as WeatherApplication).weatherDetails)
                            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.GONE
                            startActivity(intent)
                            overridePendingTransition( R.anim.slide_in_up, R.anim.stay)
                        },
                        {
                            Toast.makeText(this, "Data not available", Toast.LENGTH_LONG).show()
                            Handler(mainLooper).postDelayed({ finish() }, 3000)
                        }
                )
        )
    }

    private fun buildConfigUrlForecast(): String {
        val baseUrl = resources.getString(R.string.api_base_url_forecast)
        val api_count = resources.getString(R.string.api_count)
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$baseUrl$city&$api_count&$api_key"
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
