package com.example.pedrofialho.myweatherapp.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import com.example.pedrofialho.myweatherapp.R

class   CreditsActivity : AppCompatActivity() {
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
        setContentView(R.layout.activity_credits)

        mToolbar = findViewById(R.id.toolbar) as Toolbar

        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        (findViewById(R.id.tweahLogo) as ImageView).setOnClickListener {
            val url = Uri.parse(resources.getString(R.string.credits_data_source_url))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }
        mToolbar.setNavigationOnClickListener {
            finish()
            overridePendingTransition(0,R.anim.slide_right)
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
            true
        }
        R.id.action_settings -> {
            startActivity(Intent(this,SettingsActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}