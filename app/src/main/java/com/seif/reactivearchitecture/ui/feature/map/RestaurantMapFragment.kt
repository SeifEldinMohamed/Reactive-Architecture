package com.seif.reactivearchitecture.ui.feature.map

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.seif.reactivearchitecture.R
import permissions.dispatcher.*
import com.seif.reactivearchitecture.core.common.BaseFragment
import timber.log.Timber
import java.lang.Compiler.enable

@RuntimePermissions
class RestaurantMapFragment : BaseFragment(), OnMapReadyCallback {
        private lateinit var map:GoogleMap
   // private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

    //}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    // MARK -- handle permissions

    @NeedsPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) // request permission
    fun getCurrentLocation() {
        if (isLocationEnable()) {
           getLatKnownLocation {
                // log the location
                Timber.e("available lat , long: ${it.latitude} ${it.longitude}") // e: error
//                //call foursquare api to get restaurants
//                val currentLatLng = LatLng(it.latitude ,it.longitude)
//                val currentBounds = googleMap?.projection?.visibleRegion?.latLngBounds
//                if (currentBounds!=null && currentLatLng!=null)
//                    mapViewModel.getRestaurants(RequestDto(currentLatLng,currentBounds))
            }
        } else {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.location_not_enabled))
                .setMessage(getString(R.string.enable_location))
                .setPositiveButton(getString(R.string.enable)) { dialog, _ ->
                    // open settings screen
                  //  openSettingsScreen()
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.deny)) { dialog, _ ->
                    dialog.dismiss()
               }
                .show()
        }
    }


    @OnShowRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) //
    fun OnRationalAskLocation(request: PermissionRequest) {
//        MaterialAlertDialogBuilder(getRootActivity())
//            .setMessage(getString(R.string.location_alert))
//            .setPositiveButton(getString(R.string.accept)) { dialog, _ ->
//                request.proceed()
//                dialog.dismiss()
//            }
//            .setNegativeButton(getString(R.string.deny)) { dialog, _ ->
//                request.cancel()
//                dialog.dismiss()
//            }.show()

    }

    @OnPermissionDenied(android.Manifest.permission.ACCESS_FINE_LOCATION) // when permission denied
    fun OnDenyAskLocation() {
//        Toast.makeText(getRootActivity(), getString(R.string.location_denied), Toast.LENGTH_SHORT)
//            .show()
    }

    @OnNeverAskAgain(android.Manifest.permission.ACCESS_FINE_LOCATION) // used when user click don't ask again
    fun OnNeverAskLocation() {
//        val openApplicationSettings = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        openApplicationSettings.data = Uri.fromParts("package", activity?.packageName, null)
//        applicationSettingsScreen.launch(openApplicationSettings)
    }


    // MARK end
}