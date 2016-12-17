package com.example.pedrofialho.myweatherapp.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.pedrofialho.myweatherapp.model.WeatherDetails


class NotificationMessage : BroadcastReceiver(){
    lateinit var weather_details: WeatherDetails

    @Override
    override fun onReceive(context : Context?, it : Intent) {
        Log.v("Pedro","Receiver working")
            val serviceNoti = Intent(context,NotificationService::class.java)
//            weather_details = it.getParcelableExtra("weather_details_extra")
           // serviceNoti.putExtra("weather_details_extra",weather_details)
            context?.startService(serviceNoti)
    }

}