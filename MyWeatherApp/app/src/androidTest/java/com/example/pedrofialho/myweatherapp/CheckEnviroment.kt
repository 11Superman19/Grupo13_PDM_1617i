package com.example.pedrofialho.myweatherapp

import android.content.Context
import android.content.pm.PackageManager
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class checkEnviroment {

    private lateinit var targetContext : Context


    @Before
    fun prepare(){
        //Contexto da aplicação
        targetContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    @Throws (Exception::class)
    fun test_useAppContext() {
        Assert.assertEquals(APP_PACKAGE, targetContext.packageName)
    }

    @Test
    fun test_checkPermission(){
        Assert.assertEquals(
                PackageManager.PERMISSION_GRANTED,
                targetContext.packageManager.checkPermission(INTERNET_PERMISSION, APP_PACKAGE)
        )
    }
}