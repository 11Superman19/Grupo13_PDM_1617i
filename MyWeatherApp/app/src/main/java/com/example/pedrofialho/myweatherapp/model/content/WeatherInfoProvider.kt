package com.example.pedrofialho.myweatherapp.model.content

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.support.annotation.MainThread

/**
 * Created by Tiago on 12/12/2016.
 */

class WeatherInfoProvider : ContentProvider(){

    companion object {
        const val AUTHORITY = "isel.pdm.demos.mymoviedb"
        const val WEATHER_TABLE_PATH = "weather"
        const val FORECAST_TABLE_PATH = "weather_list"

        const val UPCOMING_CONTENT = "content://$AUTHORITY/$WEATHER_TABLE_PATH"
        val WEATHER_CONTENT_URI: Uri = Uri.parse(UPCOMING_CONTENT)
        const val EXHIBITION_CONTENT = "content://$AUTHORITY/$FORECAST_TABLE_PATH"
        val FORECAST_CONTENT_URI: Uri = Uri.parse(EXHIBITION_CONTENT)

        val WEATHER_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/movies"
        val WEATHER_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/movie"

        const val COLUMN_ID = "_ID"
        const val COLUMN_NAME = "NAME"
        const val COLUMN_COD = "CODE"
        const val COLUMN_WEATHER_DESC = "WEATHER DESCRIPTION"
        const val COLUMN_TEMP = "TEMPERATURE"  // main.temp
        const val COLUMN_WIND = "WIND"
        const val COLUMN_ICON = "ICON"


        const val COLUMN_ID_IDX = 0
        const val COLUMN_NAME_IDX = 1
        const val COLUMN_COD_IDX = 2
        const val COLUMN_WEATHER_DESC_IDX = 3
        const val COLUMN_TEMP_IDX = 4
        const val COLUMN_WIND_IDX = 5
        const val COLUMN_ICON_IDX= 6


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

        private fun createTable(db : SQLiteDatabase?, tableName : String){
            val CREATE_CMD = "CREATE TABLE $tableName ( "+
                    "$COLUMN_ID INTEGER PRIMARY KEY , " +
                    "$COLUMN_NAME TEXT NOT NULL , " +
                    "$COLUMN_COD INTEGER NOT NULL , " +
                    "$COLUMN_WEATHER_DESC TEXT NOT NULL , " +
                    "$COLUMN_TEMP FLOAT NOT NULL , " +
                    "$COLUMN_WIND FLOAT NOT NULL)"
            db?.execSQL(CREATE_CMD)
        }

        private fun deleteTable(db : SQLiteDatabase?, tableName: String) {
            val CREATE_CMD = "DELETE TABLE IF EXISTS $tableName"
            db?.execSQL(CREATE_CMD)
        }


        override fun onCreate(db : SQLiteDatabase?) {
            if (WEATHER_TABLE_NAME == null)deleteTable(db, WEATHER_TABLE_NAME)
            else createTable(db, WEATHER_TABLE_NAME)
            if (FORECAST_TABLE_NAME == null)deleteTable(db, FORECAST_TABLE_NAME)
            else createTable(db, FORECAST_TABLE_NAME)
        }

        override fun onUpgrade(db : SQLiteDatabase?, oldVersion : Int, newVersion : Int) {
            if(newVersion>oldVersion){
                deleteTable(db, WEATHER_TABLE_NAME)
                createTable(db, WEATHER_TABLE_NAME)
                deleteTable(db, FORECAST_TABLE_NAME)
                createTable(db, FORECAST_TABLE_NAME)
            }
        }

    }

    @Volatile private lateinit var dbHelper: WeatherInfoDbHelper
    @Volatile private lateinit var uriMatcher: UriMatcher


    override fun insert(p0: Uri?, p1: ContentValues?): Uri {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun query(p0: Uri?, p1: Array<out String>?, p2: String?, p3: Array<out String>?, p4: String?): Cursor {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @MainThread
    override fun onCreate(): Boolean {
        dbHelper = WeatherInfoDbHelper()
        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        with (uriMatcher) {
            addURI(AUTHORITY, WEATHER_TABLE_PATH, WEATHER_LIST_CODE)
            addURI(AUTHORITY, "$WEATHER_TABLE_PATH/#", WEATHER_ITEM_CODE)
            addURI(AUTHORITY, FORECAST_TABLE_PATH, FORECAST_LIST_CODE)
            addURI(AUTHORITY, "$FORECAST_TABLE_PATH/#", FORECAST_ITEM_CODE)
        }
        return true
    }


    override fun update(URI: Uri?, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(uri : Uri?, selection : String?, selectionArgs : Array<out String>?): Int {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getType(p0: Uri?): String {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.

    }

}