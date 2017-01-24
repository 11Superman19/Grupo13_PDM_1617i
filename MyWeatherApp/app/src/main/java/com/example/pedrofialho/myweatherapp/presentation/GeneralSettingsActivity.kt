package com.example.pedrofialho.myweatherapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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
    lateinit var mEditBoxText : EditText
    lateinit var mFAB : ImageButton
    var isEdit = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_general_settings)

        mToolbar = findViewById(R.id.toolbar) as Toolbar
        city_edit = findViewById(R.id.city) as TextView
        spinner = findViewById(R.id.spinner) as Spinner
        mEditBoxText = findViewById(R.id.editText) as EditText
        mFAB = findViewById(R.id.addOption) as ImageButton
        arrayList = ArrayList<String>()
        mFAB.setOnClickListener {
            if(isEdit){
                mEditBoxText.visibility = EditText.VISIBLE
                mFAB.setImageResource(android.R.drawable.ic_delete)
            }else{
                mEditBoxText.visibility = EditText.INVISIBLE
                mFAB.setImageResource(android.R.drawable.ic_input_add)
            }
            isEdit = !isEdit
        }
        mEditBoxText.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE ||
                    keyEvent.action == KeyEvent.ACTION_DOWN && keyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                // the user is done typing.
                val text = mEditBoxText.text.toString()
                val mgr = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                mgr.hideSoftInputFromWindow(mEditBoxText.windowToken,0)
                mEditBoxText.text.clear()
                Toast.makeText(this@GeneralSettingsActivity,text+" was added to the options",Toast.LENGTH_SHORT).show()
                setAdapter()
                return@OnEditorActionListener true // consume.
            }
            false
        })
        addFirstParam()
        setAdapter()

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

    private fun addFirstParam() {
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
    }

    private fun setAdapter() {
        val option = getSharedPreferences((application as WeatherApplication).PREFS_NAME,0).getString("option","")
        if(option!="")arrayList.add(option)
        arrayList.sort()
        mAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList)

        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = mAdapter
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
        editor.putString("option",parent.getItemAtPosition(position).toString())
        editor.apply()
    }
}
