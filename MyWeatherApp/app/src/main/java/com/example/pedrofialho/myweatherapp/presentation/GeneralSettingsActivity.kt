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
import com.example.pedrofialho.myweatherapp.WeatherApplication
import java.util.*

class GeneralSettingsActivity :AppCompatActivity(), AdapterView.OnItemSelectedListener {

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
    lateinit var city_edit : TextView
    lateinit var spinner : Spinner
    lateinit var arrayList : ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_settings)

        mToolbar = findViewById(R.id.toolbar) as Toolbar
        city_edit = findViewById(R.id.city) as TextView
        spinner = findViewById(R.id.spinner) as Spinner
        arrayList = ArrayList<String>()
        arrayList.add("Lisboa")
        arrayList.add("Madrid")
        arrayList.add("Paris")
        arrayList.add("Londres")
        arrayList.add("Roma")
        arrayList.add("Luxemburgo")
        arrayList.add("Berlin")
        arrayList.add("Bruxelas")
        arrayList.add("Atenas")
        arrayList.add("Amesterdão")
        arrayList.add("Budapest")
        arrayList.add("Moscovo")
        arrayList.add("Dublin")
        arrayList.add("Estocolmo")
        arrayList.add("Bucareste")


        mAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList)

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = mAdapter

        spinner.onItemSelectedListener = this
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

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val settings = getSharedPreferences((application as WeatherApplication).PREFS_NAME,0)
        val editor = settings.edit()
        editor.putString(parent.getItemAtPosition(position).toString(),"")
        editor.apply()
        Toast.makeText(this@GeneralSettingsActivity,"Your new Favorite Location is : "+parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show()
    }
}
