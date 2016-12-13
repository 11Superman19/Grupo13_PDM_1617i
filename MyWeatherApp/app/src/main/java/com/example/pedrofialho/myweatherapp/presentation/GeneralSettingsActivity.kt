package com.example.pedrofialho.myweatherapp.presentation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.example.pedrofialho.myweatherapp.R
import java.util.*

class GeneralSettingsActivity : AppCompatActivity() {

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

    val PREFS_NAME = "MyPrefsFile"

    var arrayList = ArrayList<String>()


    lateinit var mToolbar : Toolbar
    lateinit var city_edit : TextView
    lateinit var spinner : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_settings)

        mToolbar = findViewById(R.id.toolbar) as Toolbar
        city_edit = findViewById(R.id.city) as TextView
        spinner = findViewById(R.id.spinner) as Spinner


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
    }

    private fun finishThisActivity() {
        val settings = getSharedPreferences(PREFS_NAME, 0)
        val editor = settings.edit()
        editor.apply()
        finish()
        startActivity(Intent(this,ChoiceActivity::class.java))
        overridePendingTransition(0,R.anim.slide_right)
    }

    override fun onStop() {
        super.onStop()

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        val settings = getSharedPreferences(PREFS_NAME, 0)
        val editor = settings.edit()

        // Commit the edits!
        editor.apply()
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
