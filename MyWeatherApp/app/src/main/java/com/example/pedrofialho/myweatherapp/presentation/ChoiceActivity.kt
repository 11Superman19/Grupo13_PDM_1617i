package com.example.pedrofialho.myweatherapp.presentation

import android.Manifest
import android.animation.ObjectAnimator
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ProgressBar
import com.android.volley.toolbox.RequestFuture
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.comms.GetRequest
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import com.example.pedrofialho.myweatherapp.services.GPSTracker
import com.example.pedrofialho.myweatherapp.services.NotificationService
import java.util.*

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

    var isGps : Boolean = true

    var latitude : Double = 0.0

    var longitude : Double = 0.0

    private val INITIAL_PERMS= arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private val INITIAL_REQUEST = 1337
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        seeIfWeHaveGPSOn()
        setAlarm()
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
        val connManager = (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        val mInfoConn = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mInfoData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
        }

        (findViewById(R.id.daily) as Button).setOnClickListener {
            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.VISIBLE
            if (!mInfoConn.isConnected){
                if(!mInfoData.isConnected){
                    startActivity(WeatherDetailsActivity.createIntent(this@ChoiceActivity,(application as WeatherApplication).weatherDetails))
                }else{
                    fetchCityWeatherInfoWithAsyncTask()
                }
            }else fetchCityWeatherInfoWithAsyncTask()
            animation_daily!!.pause()
            animation_forecast!!.pause()
            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.INVISIBLE
        }
        (findViewById(R.id.forecast) as Button).setOnClickListener {
            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.VISIBLE
            if (!mInfoConn.isConnected){
                if(!mInfoData.isConnected){
                    startActivity(ForecastActivity.createIntent(this@ChoiceActivity,(application as WeatherApplication).weatherForecast))
                }else{
                    fetchWeatherForecastInfoWithAysncTask()
                }
            }else fetchWeatherForecastInfoWithAysncTask()
            animation_daily!!.pause()
            animation_forecast!!.pause()
            (findViewById(R.id.progressBar2) as ProgressBar).visibility = ProgressBar.INVISIBLE
        }
    }

    private fun seeIfWeHaveGPSOn() {
        var gps : GPSTracker
        val manager : LocationManager  = getSystemService( Context.LOCATION_SERVICE ) as LocationManager
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Do you want to see the weather for your current Location?")
                .setCancelable(false)
                .setPositiveButton("Yes", { dialogInterface, i ->
                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        if (!canAccessLocation()) {
                            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
                        }
                        buildAlertMessageNoGps()
                        gps = GPSTracker(this@ChoiceActivity)
                        //check is GPS enabled
                        if ((gps).canGetLocation){
                            isGps = true
                             latitude = gps.getLat()
                             longitude = gps.getLong()
                        }else{
                            isGps = false
                        }
                    }else{
                        if (!canAccessLocation()) {
                            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
                        }
                        gps = GPSTracker(this@ChoiceActivity)
                        if ((gps).canGetLocation){
                            isGps = true
                             latitude = gps.getLat()
                             longitude = gps.getLong()
                        }else{
                            isGps = false
                        }
                    }
                })
                .setNegativeButton("No", { dialogInterface, i ->
                    isGps = false
                    dialogInterface.cancel()
                })
        val alert : AlertDialog = builder.create()
        alert.show()
    }

    private fun canAccessLocation(): Boolean {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    private fun hasPermission(perm: String): Boolean {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm))
    }


    fun buildAlertMessageNoGps() {
        val builder  =  AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings", { dialogInterface, i ->
                    isGps = true
                    startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                })
                .setNegativeButton("Cancel", { dialogInterface, i ->
                    isGps = false
                    dialogInterface.cancel()
                })
        val alert : AlertDialog = builder.create()
        alert.show()
    }

    private fun setAlarm() {
        val alarm_cal = Calendar.getInstance()
        alarm_cal.timeInMillis = System.currentTimeMillis()
        val settings = getSharedPreferences((application as WeatherApplication).PREFS_NAME,0)
        val hour = settings.getInt("hour",0)
        val minutes = settings.getInt("minutes",0)
        alarm_cal.set(Calendar.HOUR_OF_DAY,hour)
        alarm_cal.set(Calendar.MINUTE,minutes)

        val notificationmassage = Intent(applicationContext, NotificationService::class.java)
        notificationmassage.putExtra("weather_details_extra",(application as WeatherApplication).weatherDetails)

//This is alarm manager
        val pi = PendingIntent.getService(this, 1, notificationmassage, 0)
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, alarm_cal.timeInMillis,
                AlarmManager.INTERVAL_DAY, pi)
    }

    private fun fetchWeatherForecastInfoWithAysncTask() {
        val future: RequestFuture<WeatherForecast> = RequestFuture.newFuture()
        val queue = (application as WeatherApplication).requestQueue

        (object : AsyncTask<String,Unit,WeatherForecast>(){
            override fun doInBackground(vararg params: String?): WeatherForecast {
                Log.v("Pedro","doInBackground in ${Thread.currentThread().id}")
                queue.add(GetRequest<WeatherForecast>(
                        if(!isGps) buildConfigUrlForecast("forecast/daily?q=") else buildConfigUrlForGpsForecast(longitude,latitude),
                        WeatherForecast::class.java,
                        {response -> future.onResponse(response)},
                        { error -> future.onErrorResponse(error)}
                ))
                return future.get()
            }

            override fun onPostExecute(result: WeatherForecast) {
                Log.v("Pedro", "onPostExecute in ${Thread.currentThread().id}")
                (application as WeatherApplication).weatherForecast = result
                startActivity((application as WeatherApplication).weatherForecast?.let {
                    ForecastActivity.createIntent(this@ChoiceActivity, it) })
            }
        }).execute()
    }

    private fun fetchCityWeatherInfoWithAsyncTask() {
        val future: RequestFuture<WeatherDetails> = RequestFuture.newFuture()
        val queue = (application as WeatherApplication).requestQueue

        (object : AsyncTask<String,Unit,WeatherDetails>(){
            override fun doInBackground(vararg params: String?): WeatherDetails {
                Log.v("Pedro","doInBackground in ${Thread.currentThread().id}"+"Weather Details")
                queue.add(GetRequest<WeatherDetails>(
                        if(!isGps) buildConfigUrlDetails("weather?q=") else buildConfigUrlForGpsDetails(longitude,latitude),
                        WeatherDetails::class.java,
                        {response-> future.onResponse(response)},
                        {error -> future.onErrorResponse(error)}
                ))
                return future.get()
            }

            override fun onPostExecute(result: WeatherDetails?) {
                Log.v("Pedro", "onPostExecute in ${Thread.currentThread().id}")
                (application as WeatherApplication).weatherDetails = result
                startActivity((application as WeatherApplication).weatherDetails?.let {
                    WeatherDetailsActivity.createIntent(this@ChoiceActivity, it) })
            }
        }).execute()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setMessage(resources.getString(R.string.leaving_option)).
                setPositiveButton(resources.getString(R.string.choice_yes))
                { dialog, which -> this@ChoiceActivity.finishAffinity() }.
                setNegativeButton(resources.getString(R.string.choice_no), null).setCancelable(false).
                show()
    }

    override fun onResume() {
        val settings = getSharedPreferences((application as WeatherApplication).PREFS_NAME, 0)
        val newCity = settings.getString("city", city)
        city = newCity
        if(animation_forecast!!.isPaused) animation_forecast!!.start()
        if(animation_daily!!.isPaused) animation_daily!!.start()
        super.onResume()
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

    private fun buildConfigUrlForGpsDetails(lon : Double , lat : Double) : String {
        Log.v("Showing Info From","http://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=998e2460e9dbe69907b819c7f0e1b77c")
        return "http://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=998e2460e9dbe69907b819c7f0e1b77c"
    }

    private fun buildConfigUrlForGpsForecast(lon : Double, lat: Double) : String{
        Log.v("Showing Info From","http://api.openweathermap.org/data/2.5/forecast/daily?lat=$lat&lon=$lon&cnt=16&appid=998e2460e9dbe69907b819c7f0e1b77c")
        return "http://api.openweathermap.org/data/2.5/forecast/daily?lat=$lat&lon=$lon&cnt=16&appid=998e2460e9dbe69907b819c7f0e1b77c"
    }

    private fun buildConfigUrlDetails(weatherListID: String): String {
        val baseUrl = resources.getString(R.string.api_base_url)
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$baseUrl$weatherListID$city&$api_key"
    }

    private fun buildConfigUrlForecast(weatherListID : String): String {
        val baseUrl = resources.getString(R.string.api_base_url_forecast)
        val api_count = resources.getString(R.string.api_count)
        val api_key = "${resources.getString(R.string.api_key_name)}=${resources.getString(R.string.api_key_value)}"
        return "$baseUrl$weatherListID$city&$api_count&$api_key"
    }


}
