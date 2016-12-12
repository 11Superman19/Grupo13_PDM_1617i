package com.example.pedrofialho.myweatherapp.model.content

import android.content.ContentValues
import com.example.pedrofialho.myweatherapp.model.WeatherDetails


fun WeatherDetails.toContentValues() : ContentValues{
    val result = ContentValues()
    with(WeatherInfoProvider){
        result.put(COLUMN_ID,weather.component1().id)
        result.put(COLUMN_NAME,weather.component3().description)
        result.put(COLUMN_ICON,weather.component4().icon)
    }
    return result
}

//fun WeatherForecast.toContentValues() : Array<ContentValues> =
        //list.component5().weather.map { WeatherDetails::toContentValues }.toTypedArray() esta a dar erro no array