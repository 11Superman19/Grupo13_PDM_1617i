package com.example.pedrofialho.myweatherapp.model

import android.os.Parcel
import android.os.Parcelable
import com.example.pedrofialho.myweatherapp.model.WeatherDetails.Weather
import java.text.SimpleDateFormat
import java.util.*


data class WeatherForecast(
        val cnt : Int,
        val list : List<List_Weather>
) : Parcelable  {

    data class List_Weather(
            val city: City?,
            val dt: Long,
            val temp: Temp,
            val pressure: Float,
            val humidity: Float,
            val weather: List<Weather>,
            val speed: Float,
            val deg: Float,
            val clouds: Float,
            val rain: Float,
            val snow: Float) : Parcelable{

        data class City(
                val name : String,
                val country : String
        ) : Parcelable{
            companion object{
                @JvmField @Suppress ("unused")
                val CREATOR = object : Parcelable.Creator<City> {
                    override fun createFromParcel(source: Parcel) = City(source)
                    override fun newArray(size: Int): Array<City?> = kotlin.arrayOfNulls(size)
                }
            }

            constructor(source: Parcel) : this(
                   name = source.readString(),
                    country = source.readString()
            )

            override fun writeToParcel(dest: Parcel, flags: Int) {
                dest.apply {
                    writeString(name)
                    writeString(country)
                }
            }

            override fun describeContents(): Int = 0

        }

        /**
         * Temp
         */
        data class Temp(
                val day : Float,
                val min : Float,
                val max : Float,
                val night: Float,
                val eve : Float,
                val morn : Float
        ) : Parcelable {
            companion object{
                @JvmField @Suppress ("unused")
                val CREATOR = object : Parcelable.Creator<Temp> {
                    override fun createFromParcel(source: Parcel) = Temp(source)
                    override fun newArray(size: Int): Array<Temp?> = kotlin.arrayOfNulls(size)
                }
            }

            constructor(source: Parcel) : this(
                    day = source.readFloat(),
                    min = source.readFloat(),
                    max = source.readFloat(),
                    night = source.readFloat(),
                    eve = source.readFloat(),
                    morn = source.readFloat()
            )

            override fun writeToParcel(dest: Parcel, flags: Int) {
                dest.apply {
                    writeFloat(day)
                    writeFloat(min)
                    writeFloat(max)
                    writeFloat(night)
                    writeFloat(eve)
                    writeFloat(morn)
                }
            }

            override fun describeContents(): Int = 0

        }
        override fun toString(): String{
            val date = Date(dt * 1000L) // *1000 is to convert seconds to milliseconds
            val to_return : String?

            val sdf = SimpleDateFormat("dd-MM-yyyy : "+formatedNumber(temp.max, temp.min))

            sdf.timeZone = TimeZone.getTimeZone("UTC+0")
            val formattedDate = sdf.format(date)
            to_return = formattedDate
            return to_return
        }

        private fun formatedNumber(temp1 : Float, temp2 : Float): String {
            return (((temp1-273.15).toInt().toString())+" / "+(((temp2-273.15).toInt()).toString()))
        }

        companion object{
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<List_Weather>{
                override fun createFromParcel(source: Parcel) = List_Weather(source)
                override fun newArray(size: Int): Array<List_Weather?> = kotlin.arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
                city = source.readParcelable<City>(City::class.java.classLoader),
                dt = source.readLong(),
                temp = source.readParcelable<Temp>(Temp::class.java.classLoader),
                pressure = source.readFloat(),
                humidity = source.readFloat(),
                weather = mutableListOf<Weather>().apply { source.readTypedList(this, Weather.CREATOR) },
                speed = source.readFloat(),
                deg = source.readFloat(),
                clouds = source.readFloat(),
                rain = source.readFloat(),
                snow = source.readFloat()
        )

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.apply {
                writeParcelable(city,flags)
                writeLong(dt)
                writeParcelable(temp,flags)
                writeFloat(pressure)
                writeFloat(humidity)
                writeTypedList(weather)
                writeFloat(speed)
                writeFloat(deg)
                writeFloat(clouds)
                writeFloat(rain)
                writeFloat(snow)
            }
        }

        override fun describeContents(): Int = 0


    }


    companion object{
        @JvmField @Suppress ("unused")
        val CREATOR = object : Parcelable.Creator<WeatherForecast> {
            override fun createFromParcel(source: Parcel) = WeatherForecast(source)
            override fun newArray(size: Int): Array<WeatherForecast?> = kotlin.arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            cnt = source.readInt(),
            list = mutableListOf<List_Weather>().apply { source.readTypedList(this, List_Weather.CREATOR) }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeInt(cnt)
            writeTypedList(list)
        }
    }

    override fun describeContents(): Int = 0

}