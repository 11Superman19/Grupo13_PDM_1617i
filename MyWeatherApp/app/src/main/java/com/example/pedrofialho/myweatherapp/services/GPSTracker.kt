package com.example.pedrofialho.myweatherapp.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log

open class GPSTracker(context: Context) : Service() , LocationListener{
    private val mContext: Context? = context

    init {
        getLocation()
    }


    companion object {
        lateinit var location : Location // location
    }


    // flag for GPS status
    var isGPSEnabled = false

    // flag for network status
    var isNetworkEnabled = false

    // flag for GPS status
    var canGetLocation = false

    var latitude: Double = 0.toDouble() // latitude
    var longitude: Double = 0.toDouble() // longitude

    // The minimum distance to change Updates in meters
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10.toFloat() // 10 meters

    // The minimum time between updates in milliseconds
    private val MIN_TIME_BW_UPDATES = (1000 * 60 * 1).toLong() // 1 minute

    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    fun getLocation(): Location? {
        try {
            locationManager = mContext
                    ?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // getting GPS status
            isGPSEnabled = locationManager
                    ?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!

            // getting network status
            isNetworkEnabled = locationManager
                    ?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)!!

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager?.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                    Log.d("Network", "Network")
                    if (locationManager != null) {
                        Companion.location = locationManager!!
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (Companion.location != null) {
                            latitude = Companion.location.latitude
                            longitude = Companion.location.longitude
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (Companion.location == null) {
                        locationManager?.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                        Log.d("GPS Enabled", "GPS Enabled")
                        if (locationManager != null) {
                            Companion.location = locationManager
                                    ?.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
                            if (Companion.location != null) {
                                latitude = Companion.location.latitude
                                longitude = Companion.location.longitude
                            }
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Companion.location
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onLocationChanged(location: Location?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }
}
