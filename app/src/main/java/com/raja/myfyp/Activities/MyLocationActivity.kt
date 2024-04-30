package com.raja.myfyp.Activities

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.raja.myfyp.Fragments.HomeFragment
import com.raja.myfyp.ModelClasses.PoliceModel
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityMyLocationBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import java.io.File

class MyLocationActivity : BaseActivity() {
    lateinit var binding : ActivityMyLocationBinding
    private lateinit var mapController: MapController
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityMyLocationBinding.inflate(layoutInflater)
         setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        val basePath = File(cacheDir, "osmdroid")
        val configuration = Configuration.getInstance()
        configuration.osmdroidBasePath = basePath
        configuration.osmdroidTileCache = File(configuration.osmdroidBasePath, "tile")
        configuration.userAgentValue = packageName

        binding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        binding.mapView.setBuiltInZoomControls(true)
        binding.mapView.setMultiTouchControls(true)
        mapController = binding.mapView.controller as MapController
        mapController.setZoom(15.0)



        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d("TAGlatlong", "onCreate: ${location.latitude} and ${location.longitude}")
                    val userLocation = GeoPoint(location.latitude, location.longitude)
                    mapController.setCenter(userLocation)

                    // Add a marker for the user's location (optional)
                    val userMarker = OverlayItem(
                        "My Location",
                        "This is my current location",
                        userLocation
                    )
                    val userMarkerOverlay = ItemizedOverlayWithFocus<OverlayItem>(
                        listOf(userMarker), null, this
                    )
                    binding.mapView.overlays.add(userMarkerOverlay)
                }
            }

        addPoliceStationMarkers()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)

        binding.backBtnImg.setOnClickListener {
            finish()
        }

    }

    private fun addPoliceStationMarkers() {
        // In a real scenario, you would fetch the police station data from a source
        // such as a database or an API. For simplicity, we'll add a few sample markers here.

        val policeStations = listOf(
            PoliceModel("Police Station 1", GeoPoint(30.6284478, 71.0871356),"051-26789766"),
            PoliceModel("Police Station 2", GeoPoint(31.6284281, 72.0871340),"051-26789766"),
            PoliceModel("Police Station 3", GeoPoint(30.6284298, 71.0871396),"051-26789766"),
            PoliceModel("Police Station 4", GeoPoint(34.6284278, 72.0871386),"051-26789766"),
        )

        val overlayItems = mutableListOf<OverlayItem>()
        for (station in policeStations) {
            val overlayItem = OverlayItem(station.policeStationName, "", station.policeStationLocation)
            overlayItems.add(overlayItem)
        }

        val itemizedOverlay = ItemizedOverlayWithFocus<OverlayItem>(
            overlayItems,
            object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                override fun onItemSingleTapUp(index: Int, item: OverlayItem): Boolean {
                    // Handle marker tap
                    return true
                }

                override fun onItemLongPress(index: Int, item: OverlayItem): Boolean {
                    // Handle marker long press
                    return false
                }
            }, this
        )
        itemizedOverlay.setFocusItemsOnTap(true)
        binding.mapView.overlays.add(itemizedOverlay)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove all overlays from the map view to prevent the UnsupportedOperationException
        binding.mapView.overlays.clear()
        binding.mapView.onDetach()
    }



}