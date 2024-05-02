package com.raja.myfyp.Fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.raja.myfyp.Activities.MainActivity
import com.raja.myfyp.BottomSheet.ExitBottomSheet
import com.raja.myfyp.BottomSheet.NearbyPoliceStationBottomSheet
import com.raja.myfyp.Interfaces.SelectedPoliceStationListner
import com.raja.myfyp.ModelClasses.PoliceModel
import com.raja.myfyp.R
import com.raja.myfyp.databinding.FragmentEmergencyBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import java.io.File

class EmergencyFragment : Fragment(), SelectedPoliceStationListner {
    private lateinit var mapController: MapController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var policeStations: List<PoliceModel>? = null
    private var isBottomSheetVisible = false

    lateinit var emergencyBinding: FragmentEmergencyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        emergencyBinding = FragmentEmergencyBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        emergencyBinding.viewListCardId.setOnClickListener {
            Log.d("TAG_listSize", "onCreateView: ${policeStations?.size}")
            if (!isBottomSheetVisible) {
                showBottomSheet()
            }
        }

        return emergencyBinding!!.getRoot()
    }

    private fun addPoliceStationMarkers() {
        // In a real scenario, you would fetch the police station data from a source
        // such as a database or an API. For simplicity, we'll add a few sample markers here.
        /*  policeStations?.let {
              it.add(
                  PoliceModel("Police Station 1", GeoPoint(30.6284478, 71.0871356), "051-26789766"),
              )
              it.add(
                  PoliceModel("Police Station 2", GeoPoint(31.6284281, 72.0871340), "051-26789766"),
              )
              it.add(
                  PoliceModel("Police Station 3", GeoPoint(30.6284298, 71.0871396), "051-26789766"),
              )
              it.add(
                  PoliceModel("Police Station 4", GeoPoint(34.6284278, 72.0871386), "051-26789766"),
              )
          }*/

        policeStations = listOf(
            PoliceModel("CIA Police Station, I-9-3, I-9, Islamabad, ICT, Pakistan", GeoPoint(33.658106396890766, 73.05719768251994), "051-4444791"),
            PoliceModel("Model Police Station Industrial Area I-9", GeoPoint(33.66081970300909, 73.06395828436715), "051-9258877"),
            PoliceModel("MX8W+MJJ, Shabbir Sharif Road, G-11 Markaz", GeoPoint(33.66696929489423, 72.99670828621443), "051-9330189"),
            PoliceModel("Police Station Sumbal G-13", GeoPoint(33.65053979057601,  72.96226936533131), "051-26789766"),
            PoliceModel("Police Station G-9 Markaz", GeoPoint(33.68696598219266,  73.0346298960153), "051-9334091"),
            PoliceModel("National Police Bureau, G-6/1 Markaz", GeoPoint(33.71415763318075, 73.08598602669889), "0519330189"),
            PoliceModel("Secretariat Police Station, Isfahani Rd", GeoPoint(33.72757443413244, 73.10703432300532), "051-9209132"),
            PoliceModel("Police Station Sabzi Mandi", GeoPoint(33.63798867620023, 73.02551670766017), "051-9258840"),
            PoliceModel("Police Headquarters ICT Police Lines Islamabad", GeoPoint(33.652986927512714, 73.01128904203735), "0519330189"),
        )

        val overlayItems = mutableListOf<OverlayItem>()
        policeStations?.let {
            for (station in it) {
                val overlayItem =
                    OverlayItem(station.policeStationName, "", station.policeStationLocation)
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
                }, requireContext()
            )
            itemizedOverlay.setFocusItemsOnTap(true)
            emergencyBinding.mapView.overlays.add(itemizedOverlay)
        }
    }


    override fun onPause() {
        super.onPause()
//        emergencyBinding.mapView.onPause()
        emergencyBinding.mapView.overlays.clear()
        emergencyBinding.mapView.onDetach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove all overlays from the map view to prevent the UnsupportedOperationException
        emergencyBinding.mapView.overlays.clear()
        emergencyBinding.mapView.onDetach()
    }


    companion object {
        fun newInstance(): EmergencyFragment {
            return EmergencyFragment()
        }
    }

    override fun onResume() {
        super.onResume()

        val basePath = File(requireContext().cacheDir, "osmdroid")
        val configuration = Configuration.getInstance()
        configuration.osmdroidBasePath = basePath
        configuration.osmdroidTileCache = File(configuration.osmdroidBasePath, "tile")
        configuration.userAgentValue = requireContext().packageName

        emergencyBinding.mapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        emergencyBinding.mapView.setBuiltInZoomControls(true)
        emergencyBinding.mapView.setMultiTouchControls(true)
        mapController = emergencyBinding.mapView.controller as MapController
        mapController.setZoom(15.0)


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
                        listOf(userMarker), null, requireContext()
                    )
                    emergencyBinding.mapView.overlays.add(userMarkerOverlay)
                }
            }

        addPoliceStationMarkers()

        val ctxt = (requireContext() as MainActivity)
        ctxt.setTitleFrgment(requireContext().getString(R.string.emergency))
    }

    fun showBottomSheet() {
        val bottomSheetFragment =
            NearbyPoliceStationBottomSheet.getInstance(requireContext(), policeStations)
        bottomSheetFragment.setListener(this)
        bottomSheetFragment.show(childFragmentManager, "effects")
        isBottomSheetVisible = true

    }

    override fun selectedStation(policeStation: PoliceModel?) {
    }

    override fun dismissBottomSheetCallback(isDismissed: Boolean) {
        if (isDismissed) {
            isBottomSheetVisible = false
        }
    }

}