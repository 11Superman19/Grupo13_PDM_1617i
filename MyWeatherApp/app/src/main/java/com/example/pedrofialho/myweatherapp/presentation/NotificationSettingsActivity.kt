package com.example.pedrofialho.myweatherapp.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.CheckBox
import android.widget.TimePicker
import com.example.pedrofialho.myweatherapp.R

class NotificationSettingsActivity : AppCompatActivity() {
    lateinit var check : CheckBox
    lateinit var timer : TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)

        check = findViewById(R.id.checkBox) as CheckBox
        timer = findViewById(R.id.timePicker) as TimePicker

        check.setOnClickListener {
            if (check.isChecked){
                timer.visibility=TimePicker.VISIBLE
            }
            else timer.visibility=TimePicker.INVISIBLE
        }
    }
}
