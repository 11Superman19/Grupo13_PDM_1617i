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
        var location : Location? = null // location
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
    lateinit var locationManager: LocationManager

    fun getLocation(): Location? {
        try {
            locationManager = mContext
                    ?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                    Log.d("Network", "Network")
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (location != null) {
                            latitude = location?.latitude as Double
                            longitude = location?.longitude as Double

                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this)
                        Log.d("GPS Enabled", "GPS Enabled")
                            location = getLastKnownLocation()
                            if (location != null) {
                                latitude = location?.latitude as Double
                                longitude = location?.longitude as Double
                        }
                    }
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    private fun getLastKnownLocation(): Location? {
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null
        if (providers != null) {
            for (provider in providers) {
                val l = locationManager.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    // Found best last known location: %s", l);
                    bestLocation = l
                }
            }
        }
        return bestLocation
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    fun stopUsingGPS() {
            locationManager.removeUpdates(this@GPSTracker)
    }

    /**
     * Function to get latitude
     */
    fun getLat(): Double {
        if (location != null) {
            latitude = location?.latitude as Double
        }
        return latitude
    }

    /**
     * Function to get longitude
     */
    fun getLong(): Double {
        if (location != null) {
            longitude = location?.longitude as Double
        }

        // return longitude
        return longitude
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * *
     */
    fun canGetLocation(): Boolean {
        return this.canGetLocation
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
