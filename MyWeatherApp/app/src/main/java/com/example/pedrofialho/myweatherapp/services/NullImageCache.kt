package com.example.pedrofialho.myweatherapp.services

import android.graphics.Bitmap
import com.android.volley.toolbox.ImageLoader

class NullImageCache : ImageLoader.ImageCache {

    override fun getBitmap(url: String?): Bitmap? = null
    override fun putBitmap(url: String?, bitmap: Bitmap?) = Unit
}

