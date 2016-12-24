package com.example.pedrofialho.myweatherapp.model.content

import android.content.ContentValues
import android.database.Cursor
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast
import java.util.*

//retirar os pontos quando souber meter objetos e listas
fun WeatherDetails.toContentValues() : ContentValues{
    val result = ContentValues()
    with(WeatherInfoProvider){
        result.put(COLUMN_ID, weather[0].id)
        result.put(COLUMN_HUMIDITY, main.humidity)
        result.put(COLUMN_WEATHER_DESC, weather[0].main)
        result.put(COLUMN_PRESSURE,main.pressure)
        result.put(COLUMN_TEMP,main.temp)
        result.put(COLUMN_TEMP_MAX,main.temp_max)
        result.put(COLUMN_TEMP_MIN,main.temp_min)
        result.put(COLUMN_CLOUDS,clouds?.all)
        result.put(COLUMN_RAIN, rain?.`3h`)
        result.put(COLUMN_SNOW,snow?.`3h`)
        result.put(COLUMN_WIND, wind?.speed)
        result.put(COLUMN_ICON,weather[0].icon)
    }
    return result
}

fun WeatherForecast.toContentValues() : ContentValues{
    val result = ContentValues()
    with(WeatherInfoProvider){
      /*  result.put(COLUMN_ID, list[0].weather[0].id)
        result.put(COLUMN_HUMIDITY, list[0].humidity)
        result.put(COLUMN_WEATHER_DESC, list[0].weather[0].main)
        result.put(COLUMN_PRESSURE,list[0].pressure)
        result.put(COLUMN_TEMP,list[0].temp.day)
        result.put(COLUMN_TEMP_MAX,list[0].temp.max)
        result.put(COLUMN_TEMP_MIN,list[0].temp.min)
        result.put(COLUMN_CLOUDS,list[0].clouds)
        result.put(COLUMN_RAIN, list[0].rain)
        result.put(COLUMN_CNT,cnt)
        result.put(COLUMN_DT,list[0].dt)*/
        var i = 0
       while (i < list.size){
            result.put(COLUMN_ID, list[i].weather[0].id)
            result.put(COLUMN_HUMIDITY, list[i].humidity)
            result.put(COLUMN_WEATHER_DESC, list[i].weather[0].main)
            result.put(COLUMN_PRESSURE,list[i].pressure)
            result.put(COLUMN_TEMP,list[i].temp.day)
            result.put(COLUMN_TEMP_MAX,list[i].temp.max)
            result.put(COLUMN_TEMP_MIN,list[i].temp.min)
            result.put(COLUMN_CLOUDS,list[i].clouds)
            result.put(COLUMN_RAIN, list[i].rain)
            result.put(COLUMN_CNT,cnt)
            result.put(COLUMN_DT,list[i].dt)
           ++i
        }
    }
    return result
}


fun toForecastDetail(cursor: Cursor):WeatherForecast{
    with(WeatherInfoProvider.Companion){
        return WeatherForecast(
                cnt = cursor.getInt(COLUMN_CNT_IDX),
                list = cursor.toForecastDetail()
        )
    }
}
fun Cursor.toForecastDetail():List<WeatherForecast.List_Weather> {
    val cursorIterator = object : AbstractIterator<WeatherForecast.List_Weather>() {
        override fun computeNext() {
            when (isAfterLast) {
                true -> done()
                false -> setNext(toForecastList(this@toForecastDetail))
            }
        }

    }
    return mutableListOf<WeatherForecast.List_Weather>().let { it.addAll(Iterable { cursorIterator });it }
}

private fun toForecastList(cursor: Cursor) : WeatherForecast.List_Weather{
    with(WeatherInfoProvider.Companion) {
        return WeatherForecast.List_Weather(
                dt = cursor.getLong(COLUMN_DT_IDX),
                temp = toTempObject(cursor),
                pressure = cursor.getFloat(COLUMN_PRESSURE_IDX),
                humidity = cursor.getFloat(COLUMN_HUMIDITY_IDX),
                weather = cursor.toWeatherList(),
                speed = cursor.getFloat(COLUMN_WIND_IDX),
                deg = 0.0.toFloat(),
                clouds = cursor.getFloat(COLUMN_CLOUDS_IDX),
                rain = cursor.getFloat(COLUMN_RAIN_IDX),
                snow = cursor.getFloat(COLUMN_SNOW_IDX)
        )
    }
}

fun toTempObject(cursor: Cursor): WeatherForecast.List_Weather.Temp {
    with(WeatherInfoProvider.Companion){
        return WeatherForecast.List_Weather.Temp(
                day = cursor.getFloat(COLUMN_TEMP_IDX),
                min = cursor.getFloat(COLUMN_TEMP_MIN_IDX),
                max = cursor.getFloat(COLUMN_TEMP_MAX_IDX),
                night = 0.0.toFloat(),
                eve = 0.0.toFloat(),
                morn = 0.0.toFloat()
        )
    }
}
fun toWeatherDetail(cursor : Cursor): WeatherDetails{
    with(WeatherInfoProvider.Companion){
        return WeatherDetails(
                id = cursor.getInt(COLUMN_ID_IDX),
                name = "Lisbon",//meter cidade por default
                cod = 0,
                weather = cursor.toWeatherList(),
                main = toMainObject(cursor),
                wind = toWindObject(cursor),
                clouds = toCloudsObject(cursor),
                rain = toRainObject(cursor),
                snow = toSnowObject(cursor),
                dt = 0
        )
    }
}

fun toSnowObject(cursor: Cursor): WeatherDetails.Snow? {
    with(WeatherInfoProvider.Companion){
        return WeatherDetails.Snow(
                `3h` = cursor.getInt(COLUMN_SNOW_IDX)
        )
    }
}

fun toRainObject(cursor: Cursor): WeatherDetails.Rain? {
    with(WeatherInfoProvider.Companion){
        return WeatherDetails.Rain(
                `3h` = cursor.getInt(COLUMN_RAIN_IDX)
        )
    }
}

fun toCloudsObject(cursor: Cursor): WeatherDetails.Clouds? {
    with(WeatherInfoProvider.Companion){
        return WeatherDetails.Clouds(
                all = cursor.getInt(COLUMN_CLOUDS_IDX)
        )
    }
}

fun toWindObject(cursor: Cursor): WeatherDetails.Wind? {
    with(WeatherInfoProvider.Companion){
        return WeatherDetails.Wind(
                speed = cursor.getDouble(COLUMN_WIND_IDX),
                deg = 0.0
        )
    }
}


//ver questao de ser weather details ou forecast ou fazer para os dois
fun toMainObject(cursor: Cursor) : WeatherDetails.Main{
    with(WeatherInfoProvider.Companion){
        return WeatherDetails.Main(
                temp = cursor.getFloat(COLUMN_TEMP_IDX),
                pressure = cursor.getDouble(COLUMN_PRESSURE_IDX),
                humidity = cursor.getInt(COLUMN_HUMIDITY_IDX),
                temp_min = cursor.getFloat(COLUMN_TEMP_MIN_IDX),
                temp_max = cursor.getFloat(COLUMN_TEMP_MAX_IDX),
                sea_level = 0,
                grnd_level = 0.0
        )
    }
}

private fun toWeather(cursor: Cursor) : WeatherDetails.Weather{
    with(WeatherInfoProvider.Companion){
        return WeatherDetails.Weather(
                id = 0,
                main = cursor.getString(COLUMN_WEATHER_DESC_IDX),
                description = "",
                icon = cursor.getString(COLUMN_ICON_IDX)
        )
    }
}
fun Cursor.toWeatherList() : List<WeatherDetails.Weather>{
    val weather = ArrayList<WeatherDetails.Weather>()
    weather.add(toWeather(this@toWeatherList))
    return weather
    /*val cursorIterator = object : AbstractIterator<WeatherDetails.Weather>(){
        override fun computeNext() {
            when(isAfterLast){
                true -> done()
                false -> setNext(toWeather(this@toWeatherList))
            }
        }
    }
    return mutableListOf<WeatherDetails.Weather>().let { it.addAll(Iterable { cursorIterator });it }*/
}