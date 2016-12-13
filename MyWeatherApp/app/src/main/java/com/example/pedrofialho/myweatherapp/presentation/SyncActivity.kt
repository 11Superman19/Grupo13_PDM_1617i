package com.example.pedrofialho.myweatherapp.presentation

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.pedrofialho.myweatherapp.R
import java.util.*

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

    lateinit var mAdapter: ArrayAdapter<String>

    lateinit var mToolbar : Toolbar
    lateinit var wifi : RadioButton
    lateinit var dados : RadioButton
    lateinit var spinner : Spinner


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        mToolbar = findViewById(R.id.toolbar) as Toolbar
        wifi = findViewById(R.id.wifi) as RadioButton
        dados = findViewById(R.id.mobile) as RadioButton

        spinner = findViewById(R.id.percentagem) as Spinner
        val arrayList = ArrayList<String>()
        arrayList.add("100")
        arrayList.add("90")
        arrayList.add("80")
        arrayList.add("70")
        arrayList.add("60")
        arrayList.add("50")
        arrayList.add("40")
        arrayList.add("30")
        mAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList)

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = mAdapter
        spinner.onItemSelectedListener = this



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

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        Toast.makeText(this,""+parent.getItemAtPosition(position), Toast.LENGTH_LONG).show()
    }


}
