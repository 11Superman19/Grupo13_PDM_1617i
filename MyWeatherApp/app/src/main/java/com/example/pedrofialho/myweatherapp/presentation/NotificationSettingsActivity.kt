package com.example.pedrofialho.myweatherapp.presentation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TimePicker
import com.example.pedrofialho.myweatherapp.R
import com.example.pedrofialho.myweatherapp.WeatherApplication

class NotificationSettingsActivity : AppCompatActivity() {
    lateinit var check : CheckBox
    lateinit var timer : TimePicker
    lateinit var button : Button
    lateinit var mToolbar : Toolbar
    /**
     * @property actionBarId the identifier of the toolbar as specified in the activity layout, or
     * null if the activity does not include a toolbar
     */
    val actionBarId: Int? = R.id.toolbarNotification

    /**
     * @property actionBarMenuResId the menu resource identifier that specifies the toolbar's
     * contents, or null if the activity does not include a toolbar
     */
    val actionBarMenuResId: Int? = R.menu.action_bar_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        mToolbar = findViewById(R.id.toolbarNotification) as Toolbar
        check = findViewById(R.id.checkBox) as CheckBox
        timer = findViewById(R.id.timePicker) as TimePicker
        button = findViewById(R.id.done_button) as Button

        check.setOnClickListener {
            if (check.isChecked){
                timer.visibility=TimePicker.VISIBLE
                button.visibility=Button.VISIBLE

            }
            else{
                timer.visibility=TimePicker.INVISIBLE
                button.visibility=Button.INVISIBLE
            }
        }

        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        mToolbar.setNavigationOnClickListener {
            finish()
            startActivity(Intent(this,SettingsActivity::class.java))
            overridePendingTransition(0,R.anim.slide_right)
        }
        button.setOnClickListener {
            val hour = timer.hour
            val minutes = timer.minute
            val settings = getSharedPreferences((application as WeatherApplication).PREFS_NAME,0)
            val editor = settings.edit()
            editor.putInt("hour",hour)
            editor.putInt("minutes",minutes)
            editor.apply()
        }
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
