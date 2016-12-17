package com.example.pedrofialho.myweatherapp.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
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

        val intent = Intent(context, WeatherDetailsActivity::class.java)//meter extra
        val contentIntent = PendingIntent.getActivity(context, 0,
               intent , PendingIntent.FLAG_UPDATE_CURRENT)

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