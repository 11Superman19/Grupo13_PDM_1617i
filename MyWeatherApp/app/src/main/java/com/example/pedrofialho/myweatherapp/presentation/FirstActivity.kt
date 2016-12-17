package com.example.pedrofialho.myweatherapp.presentation



import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.example.pedrofialho.myweatherapp.R.layout


class FirstActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_first)
        /*
        Nao sei onde por o sitio onde lançar a notificação perguntar ao prof
         */
        val mHandler = Handler()
        mHandler.postDelayed({ startActivity(Intent(this,ChoiceActivity::class.java))}, 2000L)
    }
}
