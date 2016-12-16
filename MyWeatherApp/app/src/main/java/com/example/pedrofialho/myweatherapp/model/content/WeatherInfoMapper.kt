package com.example.pedrofialho.myweatherapp.model.content

import android.content.ContentValues
import android.database.Cursor
import com.example.pedrofialho.myweatherapp.model.WeatherDetails
import com.example.pedrofialho.myweatherapp.model.WeatherForecast

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
        result.put(COLUMN_ID, list[0].weather[0].id)
        result.put(COLUMN_HUMIDITY, list[0].humidity)
        result.put(COLUMN_WEATHER_DESC, list[0].weather[0].main)
        result.put(COLUMN_PRESSURE,list[0].pressure)
        result.put(COLUMN_TEMP,list[0].temp.day)
        result.put(COLUMN_TEMP_MAX,list[0].temp.max)
        result.put(COLUMN_TEMP_MIN,list[0].temp.min)
        result.put(COLUMN_CLOUDS,list[0].clouds)
        result.put(COLUMN_RAIN, list[0].rain)
        result.put(COLUMN_SNOW,list[0].snow)
        result.put(COLUMN_DT,cnt)
    }
    return result
}


//TODO :  Quando souber como meter objetos e listas completar
//ver se fazer para forecast tb
private fun toWeatherDetail(cursor : Cursor): WeatherDetails{
    with(WeatherInfoProvider.Companion){
        return WeatherDetails(
                id = cursor.getInt(COLUMN_ID_IDX),
                name = "Lisbon",
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

private fun toWeatherList(cursor: Cursor) : WeatherDetails.Weather{
        with(WeatherInfoProvider.Companion){
            return WeatherDetails.Weather(
                    id = 0,
                    main = cursor.getString(COLUMN_WEATHER_DESC_IDX),
                    description = "",
                    icon = cursor.getString(COLUMN_ICON_IDX)
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

fun Cursor.toWeatherList() : List<WeatherDetails.Weather>{

    val cursorIterator = object : AbstractIterator<WeatherDetails.Weather>(){
        override fun computeNext() {
            when(isAfterLast){
                true -> done()
                false -> setNext(toWeatherList(this@toWeatherList))
            }
        }

    }
    return mutableListOf<WeatherDetails.Weather>().let { it.addAll(Iterable { cursorIterator });it }
}