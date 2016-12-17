package com.example.pedrofialho.myweatherapp.presentation



import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.pedrofialho.myweatherapp.R.layout
import com.example.pedrofialho.myweatherapp.WeatherApplication
import com.example.pedrofialho.myweatherapp.services.NotificationMessage
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
        alarm_cal.set(Calendar.HOUR,hour)
        alarm_cal.set(Calendar.MINUTE,minutes)
        Toast.makeText(this,""+hour,Toast.LENGTH_LONG).show()


        val notificationmassage = Intent(applicationContext, NotificationMessage::class.java)

//This is alarm manager
        val pi = PendingIntent.getService(this, 0, notificationmassage, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setRepeating(AlarmManager.RTC_WAKEUP, alarm_cal.timeInMillis,
                AlarmManager.INTERVAL_DAY, pi)





        val mHandler = Handler()
        mHandler.postDelayed({ startActivity(Intent(this,ChoiceActivity::class.java))}, 2000L)
    }
}
