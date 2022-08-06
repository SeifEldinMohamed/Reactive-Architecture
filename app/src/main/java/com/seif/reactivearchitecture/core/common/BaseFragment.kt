package com.seif.reactivearchitecture.core.common

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import java.util.*


open class BaseFragment : Fragment() { // to reuse getting location of user
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null // to be able to request location update and to stop location update
    private var locationRequest: LocationRequest? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpLocationClient()

    }

    private fun setUpLocationClient() {
        fusedLocationClient = FusedLocationProviderClient(requireActivity())
    }

    // private fun getRootActivity() = activity as FragmentActivity
    @SuppressLint("MissingPermission")
    fun getLatKnownLocation(onLocationAvailable: (location: Location) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null)
                onLocationAvailable(location)
            else {
                createLocationRequest(onLocationAvailable)
            }
        }
    }

    // val lambdaName: (InputType) -> ReturnType = { arguments: InputType -> body }
    private fun createLocationRequest(onLocationAvailable: (location: Location) -> Unit) { // request new location update
         locationRequest = LocationRequest.create()?.apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            // The priority of PRIORITY_HIGH_ACCURACY, combined with the ACCESS_FINE_LOCATION permission setting that you've defined in the app manifest,
            // and a fast update interval of 5000 milliseconds (5 seconds), causes the fused location provider to return location updates that are accurate to within a few feet. This approach is appropriate for mapping apps that display the location in real time.
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                   onLocationAvailable(location)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }
    private fun stopLocationUpdates(){
        if (locationCallback !=null)
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()

    }

    fun isLocationEnable() : Boolean{
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


}

/** Change Location Setting: **/
// If your app needs to request location or receive permission updates, the device needs to enable the appropriate system settings,
// such as GPS or Wi-Fi scanning. Rather than directly enabling services such as the device's GPS, your app specifies
// the required level of accuracy/power consumption and desired update interval, and the device automatically makes the appropriate changes to system settings.
// These settings are defined by the LocationRequest data object.
//
// This lesson shows you how to use the Settings Client to check which settings are enabled,
// and present the Location Settings dialog for the user to update their settings with a single tap.

/** Configure location services **/
// In order to use the location services provided by Google Play Services and the fused location provider,
// connect your app using the Settings Client, then check the current location settings and prompt the user to enable
// the required settings if needed.
//
// A pps whose features use location services must request location permissions,
// depending on the use cases of those features.

/** Set up a location request **/
// To store parameters for requests to the fused location provider, create a LocationRequest. The parameters determine the level of accuracy for location requests.
// For details of all available location request options, see the LocationRequest class reference. This lesson sets the update interval, fastest update interval, and priority, as described below:
//
// - Update interval
// setInterval() - This method sets the rate in milliseconds at which your app prefers to receive location updates.
// Note that the location updates may be somewhat faster or slower than this rate to optimize for battery usage,
// or there may be no updates at all (if the device has no connectivity, for example).
//
// - Fastest update interval
// setFastestInterval() - This method sets the fastest rate in milliseconds at which your app can handle location updates.
// Unless your app benefits from receiving updates more quickly than the rate specified in setInterval(), you don't need to call this method.
//
// - Priority
// setPriority() - This method sets the priority of the request, which gives the Google Play services
// location services a strong hint about which location sources to use. The following values are supported:
//
//  PRIORITY_BALANCED_POWER_ACCURACY - Use this setting to request location precision to within a city block, which is an accuracy of approximately 100 meters. This is considered a coarse level of accuracy, and is likely to consume less power. With this setting, the location services are likely to use WiFi and cell tower positioning. Note, however, that the choice of location provider depends on many other factors, such as which sources are available.
//  PRIORITY_HIGH_ACCURACY - Use this setting to request the most precise location possible. With this setting, the location services are more likely to use GPS to determine the location.
//  PRIORITY_LOW_POWER - Use this setting to request city-level precision, which is an accuracy of approximately 10 kilometers. This is considered a coarse level of accuracy, and is likely to consume less power.
//  PRIORITY_NO_POWER - Use this setting if you need negligible impact on power consumption, but want to receive location updates when available. With this setting, your app does not trigger any location updates, but receives locations triggered by other apps.

// Performance hint: If your app accesses the network or does other long-running work after receiving a location update, adjust the fastest interval to a slower value.
// This adjustment prevents your app from receiving updates it can't use. Once the long-running work is done, set the fastest interval back to a fast value.