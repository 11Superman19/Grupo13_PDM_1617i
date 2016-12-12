package com.example.pedrofialho.myweatherapp.model.content

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri


class WeatherInfoProvider : ContentProvider(){
    /**
     * The public contract of the provided data model.
     */

    companion object{
        const val AUTHORITY = "com.example.pedrofialho.myweatherapp"
        const val UPCOMING_TABLE_PATH = "upcoming"
        const val DETAILS_TABLE_PATH = "detail"

        const val UPCOMING_CONTENT = "content://$AUTHORITY/$UPCOMING_TABLE_PATH"
        val UPCOMING_CONTENT_URI: Uri = Uri.parse(UPCOMING_CONTENT)
        const val DETAILS_CONTENT = "content://$AUTHORITY/$DETAILS_TABLE_PATH"
        val EXHIBITION_CONTENT_URI: Uri = Uri.parse(DETAILS_CONTENT)

        val WEATHER_LIST_CONTENT_TYPE = "${ContentResolver.CURSOR_DIR_BASE_TYPE}/forecast"
        val WEATHER_ITEM_CONTENT_TYPE = "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/detail"


    }
    override fun insert(uri: Uri?, values: ContentValues?): Uri {
        throw UnsupportedOperationException("Updates are not supported")
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        throw UnsupportedOperationException("Updates are not supported")
    }

    override fun onCreate(): Boolean {
        throw UnsupportedOperationException("Updates are not supported")
    }


    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Updates are not supported")
    }

    override fun getType(uri: Uri?): String {
        throw UnsupportedOperationException("Updates are not supported")
    }


    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("Updates are not supported")
    }

}