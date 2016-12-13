package com.example.pedrofialho.myweatherapp.presentation

import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
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
        dados.setText("esgay")
        check = findViewById(R.id.checkBox) as CheckBox
        timer = findViewById(R.id.timePicker) as TimePicker

        check.setOnClickListener {
            if (check.isChecked){
                timer.visibility=TimePicker.VISIBLE
            }
            else timer.visibility=TimePicker.INVISIBLE
        }


        actionBarId?.let {
            setSupportActionBar(findViewById(it) as Toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)

        }
    }
}
