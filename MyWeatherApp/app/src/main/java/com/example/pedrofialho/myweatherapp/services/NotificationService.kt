package com.example.pedrofialho.myweatherapp.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.presentation.WeatherDetailsActivity


class NotificationService : Service(){
    lateinit var weather_details: WeatherDetails
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
      //  weather_details = intent.getParcelableExtra("weather_details_extra")
        Log.v("Pedro","Service working")
        showNotification(applicationContext)
        Toast.makeText(this, "Notification", Toast.LENGTH_LONG).show()
        return Service.START_FLAG_REDELIVERY
    }
    private fun showNotification(context : Context) {
        Log.v("notification", "visible")

        val intent = Intent(context, WeatherDetailsActivity::class.java)
       // intent.putExtra("weather_details_extra",weather_details)
        val contentIntent = PendingIntent.getActivity(context, 0,
                intent , PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.alert_light_frame)
                .setContentTitle("Alert")
                .setContentText("Refreshed Weather at your Location.")
        mBuilder.setContentIntent(contentIntent)
        mBuilder.setDefaults(Notification.DEFAULT_SOUND)
        mBuilder.setAutoCancel(true)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mNotificationManager.notify(1, mBuilder.build())
    }
}