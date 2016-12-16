package com.example.pedrofialho.myweatherapp.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Class whose instances represents weatherapi information obtained from the remote API
 *
 *
 * <p>As all other DTOs used in the application, the class implements the Parcelable contract
 * thereby allowing them to be passed between Android components and, eventually, across process
 * boundaries.</p>
 *
 *
 * @property id the weatherapi id
 * @property name name of the city
 * @property cod code of the city
 * @property weather the conditions of the weatherapi
 * @property main weatherapi details
 * @property wind the wind conditions
 * @property clouds clouds percentage
 * @property rain rain volume for the last 3 hours
 * @property snow snow volume for the last 3 hours
 * @property dt Time of data calculation, unix, UTC
 */
//retirar os nulls depois conseguir meter os idx
data class WeatherDetails(
        val id: Int,
        val name: String,
        val cod: Int,
        val weather: List<Weather>,
        val main: Main,
        val wind: Wind?,
        val clouds: Clouds?,
        val rain: Rain?,
        val snow: Snow?,
        val dt: Long

    ) : Parcelable{


    /**
     * Class whose instances represents Clouds information
     *
     * @property all percentage of the clouds
     */
    data class Clouds(val all : Int) : Parcelable{

        companion object{
            /** Factory of Clouds instances */
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Clouds>{
                override fun createFromParcel(source: Parcel) = Clouds(source)
                override fun newArray(size: Int): Array<Clouds?> = kotlin.arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
                all = source.readInt()
        )


        /**
         * Saves the instance data to the given parcel.
         * @param dest The parcel to where the data is to be saved to
         * @param flags Not used (see android documentation for further details)
         */
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.apply {
                writeInt(all)
            }
        }

        /** Not used (see android documentation for further details) */
        override fun describeContents(): Int = 0

    }

    /**
     * Class whose instances represents wind information
     *
     * @property speed Speed of the wind
     * @property deg weatherapi direction, degrees
     */
    data class Wind(
            val speed : Double,
            val deg : Double) : Parcelable{

        companion object{
            /** Factory of Wind instances */
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Wind>{
                override fun createFromParcel(source: Parcel) = Wind(source)
                override fun newArray(size:Int) : Array<Wind?> = kotlin.arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
                speed = source.readDouble(),
                deg = source.readDouble()
        )


        /**
         * Saves the instance data to the given parcel.
         * @param dest The parcel to where the data is to be saved to
         * @param flags Not used (see android documentation for further details)
         */
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.apply {
                writeDouble(speed)
                writeDouble(deg)
            }
        }

        /** Not used (see android documentation for further details) */
        override fun describeContents(): Int  = 0
    }

    /**
     * Class whose information represents the main information about the weatherapi
     *
     * @property temp Temperature. Unit default:Kelvin
     * @property pressure Atmospheric pressure
     * @property humidity humidity percentage
     * @property temp_min Minimum temperature at the moment
     * @property temp_max Maximum temperature at the moment
     * @property sea_level Atmospheric pressure on the sea level
     * @property grnd_level Atmospheric pressure on the ground level
     */
    data class Main(
            val temp : Float,
            val pressure : Double,
            val humidity : Int?,
            val temp_min : Float,
            val temp_max : Float,
            val sea_level : Int,
            val grnd_level : Double) : Parcelable{

        companion object {
            /** Factory of Main instances*/
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Main> {
                override fun createFromParcel(source: Parcel) = Main(source)
                override fun newArray(size: Int): Array<Main?> = kotlin.arrayOfNulls(size)
            }
        }

        constructor(source: Parcel) : this(
                temp = source.readFloat(),
                pressure = source.readDouble(),
                humidity = source.readInt(),
                temp_min = source.readFloat(),
                temp_max = source.readFloat(),
                sea_level = source.readInt(),
                grnd_level = source.readDouble()
        )

        /**
         * Saves the instance data to the given parcel.
         * @param dest The parcel to where the data is to be saved to
         * @param flags Not used (see android documentation for further details)
         */
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.apply {
                writeFloat(temp)
                writeDouble(pressure)
                writeInt(humidity!!)
                writeFloat(temp_min)
                writeFloat(temp_max)
                writeInt(sea_level)
                writeDouble(grnd_level)
            }
        }

        /** Not used (see android documentation for further details) */
        override fun describeContents(): Int = 0

    }

    /**
     * Class whose instances represents Weather condition codes
     *
     *@property id weatherapi condition id
     *@property main Group of weatherapi parameters
     *@property description Weather condition within the group
     *@property icon icon(image) for the weatherapi condition
     */
    data class Weather(
            val id : Int,
            val main : String,
            val description : String,
            val icon : String) : Parcelable{

        companion object {
            /**Factory of Weather instances*/
            @JvmField @Suppress ("unused")
            val CREATOR = object : Parcelable.Creator<Weather>{
                override fun createFromParcel(source : Parcel) = Weather(source)
                override fun newArray(size:Int) : Array<Weather?> = kotlin.arrayOfNulls(size)
            }
        }

        /**
         * Initiates an instance from the given parcel.
         * @param source The parcel from where the data is to be loaded from
         */
        constructor(source: Parcel) : this(
                id = source.readInt(),
                main = source.readString(),
                description = source.readString(),
                icon = source.readString()
        )
        /** Not used (see android documentation for further details) */
        override fun describeContents(): Int = 0

        /**
         * Saves the instance data to the given parcel.
         * @param dest The parcel to where the data is to be saved to
         * @param flags Not used (see android documentation for further details)
         */
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.apply {
                writeInt(id)
                writeString(main)
                writeString(description)
                writeString(icon)
            }
        }

    }


    /**
     * Class whose instances represent information about the rain
     *
     *@property `3h` represents the rain volume for the oast 3 hours
     */
    data class Rain (val `3h`: Int) : Parcelable{

        companion object {
            /**Factory of Rain instances*/
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Rain>{
                override fun createFromParcel(source : Parcel) = Rain(source)
                override fun newArray(size:Int) : Array<Rain?> = kotlin.arrayOfNulls(size)
            }
        }
        /**
         * Initiates an instance from the given parcel.
         * @param source The parcel from where the data is to be loaded from
         */
            constructor(source: Parcel) : this(
                    `3h` = source.readInt()
            )


        /**
         * Saves the instance data to the given parcel.
         * @param dest The parcel to where the data is to be saved to
         * @param flags Not used (see android documentation for further details)
         */
            override fun writeToParcel(dest: Parcel, flags: Int){
                dest.apply {
                    writeInt(`3h`)
                }
            }

        /** Not used (see android documentation for further details) */
            override fun describeContents(): Int = 0

    }

    /**
     * Class whose instances represents information about the snow
     *
     *@property '3h' represents the snow volume for the past 3 hours
     */
    data class Snow(val `3h`: Int) : Parcelable{


        companion object{
            /**Factory of Snow objects*/
            @JvmField @Suppress("unused")
            val CREATOR = object : Parcelable.Creator<Snow>{
                override fun createFromParcel(source : Parcel) = Snow(source)
                override fun newArray(size:Int) : Array<Snow?> = kotlin.arrayOfNulls(size)
            }
        }

        /**
         * Initiates an instance from the given parcel.
         * @param source The parcel from where the data is to be loaded from
         */
        constructor(source: Parcel) : this(
                `3h` = source.readInt()
        )


        /**
         * Saves the instance data to the given parcel.
         * @param dest The parcel to where the data is to be saved to
         * @param flags Not used (see android documentation for further details)
         */
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.apply {
                writeInt(`3h`)
            }
        }

        /** Not used (see android documentation for further details) */
        override fun describeContents(): Int = 0
    }

    /**
     * Information gathering the class WeatherDetails
     */

    companion object{
        /**Factory of WeatherDetails instances*/
        @JvmField @Suppress ("unused")
        val CREATOR = object : Parcelable.Creator<WeatherDetails> {
            override fun createFromParcel(source: Parcel) = WeatherDetails(source)
            override fun newArray(size: Int): Array<WeatherDetails?> = kotlin.arrayOfNulls(size)
        }
    }

    /**
     * Initiates an instance from the given parcel.
     * @param source The parcel from where the data is to be loaded from
     */
    constructor(source: Parcel) : this(
            id = source.readInt(),
            name = source.readString(),
            cod = source.readInt(),
            weather = mutableListOf<Weather>().apply { source.readTypedList(this,Weather.CREATOR) },
            main = source.readParcelable<Main>(Main::class.java.classLoader),
            wind = source.readParcelable<Wind>(Wind::class.java.classLoader),
            clouds = source.readParcelable<Clouds>(Clouds::class.java.classLoader),
            rain = source.readParcelable<Rain>(Rain::class.java.classLoader),
            snow = source.readParcelable<Snow>(Snow::class.java.classLoader),
            dt = source.readLong()
    )

    /**
     * Saves the instance data to the given parcel.
     * @param dest The parcel to where the data is to be saved to
     * @param flags Not used (see android documentation for further details)
     */
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.apply {
            writeInt(id)
            writeString(name)
            writeInt(cod)
            writeTypedList(weather)
            writeParcelable(main,flags)
            writeParcelable(wind,flags)
            writeParcelable(clouds,flags)
            writeLong(dt)
        }
    }

    /** Not used (see android documentation for further details) */
    override fun describeContents(): Int = 0
}