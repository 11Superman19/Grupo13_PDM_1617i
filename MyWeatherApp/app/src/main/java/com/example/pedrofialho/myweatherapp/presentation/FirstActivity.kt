package com.example.pedrofialho.myweatherapp.presentation



import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.pedrofialho.myweatherapp.R.layout
import com.example.pedrofialho.myweatherapp.WeatherApplication
import java.util.*

class FirstActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_first)
        /*
        Nao sei onde por o sitio onde lançar a notificação perguntar ao prof
         */
        val alarm_cal = Calendar.getInstance()
        alarm_cal.timeInMillis = System.currentTimeMillis()
        val settings = getSharedPreferences((application as WeatherApplication).PREFS_NAME,0)
        val hour = settings.getInt("hour",0)
        val minutes = settings.getInt("minutes",0)
        Toast.makeText(this,"BOAS"+hour,Toast.LENGTH_LONG).show()
        alarm_cal.set(Calendar.HOUR,hour)
        alarm_cal.set(Calendar.MINUTE,minutes)

        val mBuilder = Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_email)
                .setContentTitle("The time")
                .setContentText("Hello World!")

        val resultIntent = Intent(this,WeatherDetailsActivity::class.java)//mandar o parcelable

        val pendingIntent = PendingIntent.getActivity(
                this,
                (alarm_cal.timeInMillis.toInt()),
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

        mBuilder.setContentIntent(pendingIntent)


        val mNotificationId = 1

        val mNotifyMgr = (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)

        mNotifyMgr.notify(mNotificationId,mBuilder.build())
        val mHandler = Handler()
        mHandler.postDelayed({ startActivity(Intent(this,ChoiceActivity::class.java))}, 2000L)
    }
}
