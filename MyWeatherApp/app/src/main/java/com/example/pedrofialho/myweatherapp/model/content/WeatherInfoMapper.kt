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
        result.put(COLUMN_WEATHER_DESC, weather[0].description)
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
        result.put(COLUMN_WEATHER_DESC, list[0].weather[0].description)
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
/*private fun toWeatherDetail(cursor : Cursor): WeatherDetails{
    with(WeatherInfoProvider.Companion){
        return WeatherDetails(
                id = cursor.getInt(COLUMN_ID_IDX),
                name = cursor.getString(COLUMN_WEATHER_DESC_IDX),
                cod = 0,
                weather = null,
                main = null,
                wind = null,
                clouds = null,
                rain = null,
                snow = null,
                dt = 0
        )
    }
}*/

//ver questao de ser weather details ou forecast ou fazer para os dois
fun Cursor.toWeatherList() : List<WeatherDetails>{

    val cursorIterator = object : AbstractIterator<WeatherDetails>(){
        override fun computeNext() {
            when(isAfterLast){
                true -> done()
              //  false -> setNext(toWeatherDetail(this@toWeatherList))
            }
        }

    }
    return mutableListOf<WeatherDetails>().let { it.addAll(Iterable { cursorIterator });it }
}