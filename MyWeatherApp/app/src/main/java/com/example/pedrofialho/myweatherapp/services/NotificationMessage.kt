package com.example.pedrofialho.myweatherapp.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.pedrofialho.myweatherapp.R.mipmap.ic_launcher
import android.content.Context.NOTIFICATION_SERVICE
import android.support.v4.app.NotificationCompat
import android.R.drawable.*
import android.util.Log
import com.example.pedrofialho.myweatherapp.presentation.WeatherDetailsActivity

/**
 * Created by Tiago on 16/12/2016.
 */


class NotificationMessage : BroadcastReceiver(){

    @Override
    override fun onReceive(context : Context?, it : Intent?) {
        showNotification(context)
    }

    private fun showNotification(context: Context?) {
        Log.v("notification", "visible")

        val contentIntent = PendingIntent.getActivity(context, 0,
                Intent(context, WeatherDetailsActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.alert_light_frame)
                .setContentTitle("Alert")
                .setContentText("Refreshed Weather at your Location.")
        mBuilder.setContentIntent(contentIntent)
        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(true)
        val mNotificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }


}