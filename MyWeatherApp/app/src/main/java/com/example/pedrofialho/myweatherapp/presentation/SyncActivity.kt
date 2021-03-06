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

class SyncActivity : AppCompatActivity(),AdapterView.OnItemSelectedListener {

    /**
     * @property actionBarId the identifier of the toolbar as specified in the activity layout, or
     * null if the activity does not include a toolbar
     */
    val actionBarId: Int? = R.id.toolbarSync

    /**
     * @property actionBarMenuResId the menu resource identifier that specifies the toolbar's
     * contents, or null if the activity does not include a toolbar
     */
    val actionBarMenuResId: Int? = R.menu.action_bar_activity



    lateinit var mToolbar : Toolbar
    lateinit var connectivity : TextView
    lateinit var wifi : RadioButton
    lateinit var dados : RadioButton
    lateinit var choose : Button
    lateinit var spinner : Spinner
    lateinit var mAdapter: ArrayAdapter<String>
    var both = true
    lateinit var radioGroup : RadioGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sync)

        mToolbar = findViewById(R.id.toolbarSync) as Toolbar
        connectivity = findViewById(R.id.Connectivity) as TextView
        wifi = findViewById(R.id.wifi) as RadioButton
        dados = findViewById(R.id.mobile) as RadioButton
        radioGroup = findViewById(R.id.group) as RadioGroup
        choose = findViewById(R.id.choice) as Button
        val settings = getSharedPreferences((application as WeatherApplication).PREFS_NAME,0)
        val editor = settings.edit()
        editor.putBoolean("connectivity_both",both)
        both = settings.getBoolean("connectivity_both",false)
        if(both){
            radioGroup.check(R.id.both)
        }else if(settings.getBoolean("connectivity",true)){
            radioGroup.check(R.id.wifi)
        }else radioGroup.check(R.id.mobile)

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
        arrayList.add("20")
        arrayList.add("15")
        mAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList)

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = mAdapter
        spinner.onItemSelectedListener = this

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if (i==R.id.wifi){
                both=false
                editor.putBoolean("connectivity",true)
                editor.putBoolean("connectivity_both",both)
            }else if(i==R.id.mobile){
                both=false
                editor.putBoolean("connectivity",false)
                editor.putBoolean("connectivity_both",both)
            }else if(i==R.id.both){
                both=true
                editor.putBoolean("connectivity_both",both)
            }
        }

        choose.setOnClickListener {
            editor.apply()
            val selectedId = radioGroup.checkedRadioButtonId
            val textToPresent : String?
            if(selectedId == wifi.id) {
               textToPresent = "You choose WIFI option"
            } else if(selectedId == dados.id) {
                textToPresent = "You choose Mobile Data option"
            } else {
                textToPresent = "You choose Both options"
            }

            Toast.makeText(this@SyncActivity,textToPresent,Toast.LENGTH_SHORT).show()
            this@SyncActivity.finish()
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
        editor.putString("bateria",parent.getItemAtPosition(position).toString())
        editor.apply()
    }

}
