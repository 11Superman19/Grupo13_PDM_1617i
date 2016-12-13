package com.example.pedrofialho.myweatherapp.presentation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.TextView
import android.widget.TimePicker
import com.example.pedrofialho.myweatherapp.R

class SyncActivity : AppCompatActivity() {
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
    lateinit var connectivity : TextView
    lateinit var wifi : RadioButton
    lateinit var dados : RadioButton
    lateinit var check : CheckBox
    lateinit var timer : TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        mToolbar = findViewById(R.id.toolbar) as Toolbar
        connectivity = findViewById(R.id.Connectivity) as TextView
        wifi = findViewById(R.id.wifi) as RadioButton
        dados = findViewById(R.id.mobile) as RadioButton
        check = findViewById(R.id.checkBox) as CheckBox
        timer = findViewById(R.id.timePicker) as TimePicker

        check.setOnClickListener {
            if (check.isChecked){
                timer.visibility=TimePicker.VISIBLE
            }
            else timer.visibility=TimePicker.INVISIBLE
        }


        mToolbar.setNavigationOnClickListener {
            finish()
            startActivity(Intent(this,SettingsActivity::class.java))
            overridePendingTransition(0,R.anim.slide_right)
        }

        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)

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
