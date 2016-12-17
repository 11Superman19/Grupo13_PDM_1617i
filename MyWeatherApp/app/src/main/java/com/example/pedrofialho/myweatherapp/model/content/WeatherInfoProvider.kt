package com.example.pedrofialho.myweatherapp.model.content

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri

class WeatherInfoProvider : ContentProvider(){

    companion object {
        const val AUTHORITY = "com.example.pedrofialho.myweatherapp"
        const val WEATHER_TABLE_PATH = "weather"
        const val FORECAST_TABLE_PATH = "weather_list"

        const val UPCOMING_CONTENT = "content://$AUTHORITY/$WEATHER_TABLE_PATH"
        val WEATHER_CONTENT_URI: Uri = Uri.parse(UPCOMING_CONTENT)
        const val EXHIBITION_CONTENT = "content://$AUTHORITY/$FORECAST_TABLE_PATH"
        val FORECAST_CONTENT_URI: Uri = Uri.parse(EXHIBITION_CONTENT)

        val WEATHER_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/forecast"
        val WEATHER_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/detail"


        //SHARED INFO
        const val COLUMN_ID = "_ID"//SHARE
        const val COLUMN_HUMIDITY = "HUMIDITY"//SHARE
        const val COLUMN_WEATHER_DESC = "WEATHER_DESCRIPTION"//SHARE
        const val COLUMN_PRESSURE = "PRESSURE"//SHARE
        const val COLUMN_TEMP = "TEMPERATURE"//SHARE
        const val COLUMN_TEMP_MAX = "MAX"//SHARE
        const val COLUMN_TEMP_MIN = "MIN"//SHARE
        const val COLUMN_CLOUDS = "CLOUDS"//SHARE
        const val COLUMN_RAIN = "RAIN"//SHARE
        const val COLUMN_SNOW = "SNOW"//SHARE

        //weatherDetails INFO
        const val COLUMN_WIND = "WIND"
        const val COLUMN_ICON = "ICON"

        //weatherForecast INFO
        const val COLUMN_CNT = "CNT"
        const val COLUMN_DT = "DT"


        //shared idx
        const val COLUMN_ID_IDX = 0//SHARE
        const val COLUMN_HUMIDITY_IDX = 1 //SHARE
        const val COLUMN_WEATHER_DESC_IDX = 2 //SHARE
        const val COLUMN_PRESSURE_IDX = 3//SHARE
        const val COLUMN_TEMP_IDX = 4//SHARE
        const val COLUMN_TEMP_MAX_IDX =5//SHARE
        const val COLUMN_TEMP_MIN_IDX=6//SHARE
        const val COLUMN_CLOUDS_IDX = 7//SHARE
        const val COLUMN_RAIN_IDX = 8//SHARE
        const val COLUMN_SNOW_IDX = 9//SHARE
        //idx for weatherdetails
        const val COLUMN_WIND_IDX =10
        const val COLUMN_ICON_IDX = 11
        //IDX FOR FORECAST
        const val COLUMN_CNT_IDX = 10
        const val COLUMN_DT_IDX = 11

        // Private constants to be used by the implementation
        private const val WEATHER_TABLE_NAME = "Weather"
        private const val FORECAST_TABLE_NAME = "Forecast"

        private const val WEATHER_LIST_CODE = 1010
        private const val WEATHER_ITEM_CODE = 1011
        private const val FORECAST_LIST_CODE = 1020
        private const val FORECAST_ITEM_CODE = 1021
    }


    private inner class WeatherInfoDbHelper(version : Int = 1, dbName : String = "WEATHER_DB") :
        SQLiteOpenHelper(this@WeatherInfoProvider.context,dbName,null,version){

        private fun createDetailsTable(db : SQLiteDatabase?, tableName : String){
            val CREATE_CMD = "CREATE TABLE $tableName ( "+
                    "$COLUMN_ID INTEGER PRIMARY KEY , " +
                    "$COLUMN_HUMIDITY INTEGER , " +
                    "$COLUMN_WEATHER_DESC TEXT NOT NULL , " +
                    "$COLUMN_PRESSURE FLOAT NOT NULL , " +
                    "$COLUMN_TEMP FLOAT NOT NULL , " +
                    "$COLUMN_TEMP_MAX FLOAT NOT NULL , " +
                    "$COLUMN_TEMP_MIN FLOAT NOT NULL , " +
                    "$COLUMN_CLOUDS INTEGER , " +
                    "$COLUMN_RAIN INTEGER , " +
                    "$COLUMN_SNOW INTEGER , " +
                    "$COLUMN_WIND FLOAT NOT NULL ," +
                    "$COLUMN_ICON TEXT NOT NULL )"
            db?.execSQL(CREATE_CMD)
        }

        private fun createForecastTable(db : SQLiteDatabase?, tableName : String){
            val CREATE_CMD = "CREATE TABLE $tableName ( "+
                    "$COLUMN_ID INTEGER PRIMARY KEY , " +
                    "$COLUMN_HUMIDITY INTEGER , " +
                    "$COLUMN_WEATHER_DESC TEXT NOT NULL , " +
                    "$COLUMN_PRESSURE FLOAT NOT NULL , " +
                    "$COLUMN_TEMP FLOAT NOT NULL , " +
                    "$COLUMN_TEMP_MAX FLOAT NOT NULL , " +
                    "$COLUMN_TEMP_MIN FLOAT NOT NULL , " +
                    "$COLUMN_CLOUDS INTEGER , " +
                    "$COLUMN_RAIN INTEGER , " +
                    "$COLUMN_CNT INTEGER NOT NULL, " +
                    "$COLUMN_DT LONG NOT NULL )"
            db?.execSQL(CREATE_CMD)
        }

        private fun deleteTable(db : SQLiteDatabase?, tableName: String) {
            val DROP_CMD = "DELETE TABLE IF EXISTS $tableName"
            db?.execSQL(DROP_CMD)
        }


        override fun onCreate(db : SQLiteDatabase?) {
            createDetailsTable(db, WEATHER_TABLE_NAME)
            createForecastTable(db, FORECAST_TABLE_NAME)
        }

        override fun onUpgrade(db : SQLiteDatabase?, oldVersion : Int, newVersion : Int) {
                deleteTable(db, WEATHER_TABLE_NAME)
                createDetailsTable(db, WEATHER_TABLE_NAME)
                deleteTable(db, FORECAST_TABLE_NAME)
                createForecastTable(db, FORECAST_TABLE_NAME)
        }
    }
    /**
     * @property dbHelper The DB helper instance to be used for DB accesses.
     */
    @Volatile private lateinit var dbHelper: WeatherInfoDbHelper

    /**
     * @property uriMatcher The instance used to match an URI to its corresponding content type
     */
    @Volatile private lateinit var uriMatcher: UriMatcher

    override fun onCreate(): Boolean {
        dbHelper = WeatherInfoDbHelper()
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        with(uriMatcher){
            addURI(AUTHORITY, FORECAST_TABLE_PATH, FORECAST_LIST_CODE)
            addURI(AUTHORITY, "$FORECAST_TABLE_PATH/#", FORECAST_LIST_CODE)
            addURI(AUTHORITY, WEATHER_TABLE_PATH, WEATHER_LIST_CODE)
            addURI(AUTHORITY, "$WEATHER_TABLE_PATH/#", WEATHER_LIST_CODE)
        }
        return true
    }

    override fun getType(uri: Uri?): String  = when( uriMatcher.match(uri)){
        FORECAST_LIST_CODE, WEATHER_LIST_CODE -> WEATHER_LIST_CONTENT_TYPE
        FORECAST_ITEM_CODE, WEATHER_ITEM_CODE -> WEATHER_ITEM_CONTENT_TYPE
        else -> throw IllegalArgumentException("Uri $uri not supported")
    }

    private fun resolveTableAndSelectionInfoFromUri(uri: Uri, selection: String?, selectionArgs: Array<String>?)
            : Triple<String, String?, Array<String>?> {
        val itemSelection = "$COLUMN_ID = ${uri.pathSegments.first()}"
        return when (uriMatcher.match(uri)) {
            FORECAST_LIST_CODE -> Triple(FORECAST_TABLE_NAME, itemSelection, null)
            WEATHER_LIST_CODE -> Triple(WEATHER_TABLE_NAME, itemSelection, null)
            else -> resolveTableInfoFromUri(uri).let { Triple(it.first, selection, selectionArgs) }
        }
    }

    /**
     * Helper function used to obtain the table information (i.e. table name and path) based on the
     * given [uri]
     * @param [uri] The table URI
     * @return A [Pair] instance bearing the table name (the pair's first) and the table path
     * part (the pair's second).
     * @throws IllegalArgumentException if the received [uri] does not refer to an existing table
     */
    private fun resolveTableInfoFromUri(uri: Uri): Pair<String, String> = when (uriMatcher.match(uri)) {
        FORECAST_LIST_CODE -> Pair(FORECAST_TABLE_NAME, FORECAST_TABLE_PATH)
        WEATHER_LIST_CODE -> Pair(WEATHER_TABLE_NAME, WEATHER_TABLE_PATH)
        else -> null
    } ?: throw IllegalArgumentException("Uri $uri not supported")

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val params = resolveTableAndSelectionInfoFromUri(uri, selection, selectionArgs)
        val db = dbHelper.writableDatabase
        try {
            val deletedCount = db.delete(params.first, params.second, params.third)
            if (deletedCount != 0)
                context.contentResolver.notifyChange(uri, null)
            return deletedCount
        }
        finally {
            db.close()
        }
    }


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val tableInfo = resolveTableInfoFromUri(uri)
        val db = dbHelper.writableDatabase
        return try {
            val id = db.insert(tableInfo.first, null, values)
            if (id < 0) null else {
                context.contentResolver.notifyChange(uri, null)
                Uri.parse("content://$AUTHORITY/${tableInfo.second}/$id")
            }
        }
        finally {
            db.close()
        }
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {

        val params = resolveTableAndSelectionInfoFromUri(uri, selection, selectionArgs)
        val db = dbHelper.readableDatabase
        return try {
            db.query(params.first, projection, params.second, params.third, null, null, sortOrder)
        }
        finally {
            db.close()
        }
    }



    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Updates are not supported")
    }

}