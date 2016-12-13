package com.example.pedrofialho.myweatherapp.presentation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import com.example.pedrofialho.myweatherapp.R

class SettingsActivity : AppCompatActivity(){

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

    lateinit var general_button : ImageButton
    lateinit var connect_button : ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        mToolbar = findViewById(R.id.toolbar) as Toolbar
        general_button = findViewById(R.id.general_button) as ImageButton
        connect_button = findViewById(R.id.connect_button) as ImageButton

        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        mToolbar.setNavigationOnClickListener {
            finish()
            startActivity(Intent(this,ChoiceActivity::class.java))
            overridePendingTransition(0,R.anim.slide_right)
        }

        general_button.setOnClickListener {
            startActivity(Intent(this,GeneralSettingsActivity::class.java))
            overridePendingTransition(R.anim.stay,R.anim.slide_right)
        }

        connect_button.setOnClickListener {
            startActivity(Intent(this,SyncActivity::class.java))
            overridePendingTransition(R.anim.stay,R.anim.slide_right)
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
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
