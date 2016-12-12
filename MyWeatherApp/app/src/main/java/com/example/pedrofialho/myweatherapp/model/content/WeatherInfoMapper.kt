package com.example.pedrofialho.myweatherapp.model.content

import android.content.ContentValues
import android.database.Cursor
import com.example.pedrofialho.myweatherapp.model.WeatherDetails

//retirar os pontos quando souber meter objetos e listas
fun WeatherDetails.toContentValues() : ContentValues{
    val result = ContentValues()
    with(WeatherInfoProvider){
        result.put(COLUMN_ID,weather?.component1()?.id)
        result.put(COLUMN_NAME,weather?.component3()?.description)
        result.put(COLUMN_ICON,weather?.component4()?.icon)
    }
    return result
}

/*fun WeatherForecast.toContentValues() : Array<ContentValues> =
        list.component5().weather.map{WeatherDetails::toContentValues}.toTypedArray()
*/

//TODO :  Quando souber como meter objetos e listas completar
private fun toWeatherDetail(cursor : Cursor): WeatherDetails{
    with(WeatherInfoProvider.Companion){
        return WeatherDetails(
                id = cursor.getInt(COLUMN_ID_IDX),
                name = cursor.getString(COLUMN_NAME_IDX),
                cod = cursor.getInt(COLUMN_COD_IDX),
                weather = null,
                main = null,
                wind = null,
                clouds = null,
                rain = null,
                snow = null,
                dt = cursor.getLong(COLUMN_DT_IDX)
        )
    }
}

//ver questao de ser weather details ou forecast ou fazer para os dois
fun Cursor.toWeatherList() : List<WeatherDetails>{

    val cursorIterator = object : AbstractIterator<WeatherDetails>(){
        override fun computeNext() {
            when(isAfterLast){
                true -> done()
                false -> setNext(toWeatherDetail(this@toWeatherList))
            }
        }

    }
    return mutableListOf<WeatherDetails>().let { it.addAll(Iterable { cursorIterator });it }
}