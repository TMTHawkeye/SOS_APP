package com.raja.myfyp.ModelClasses

import org.osmdroid.util.GeoPoint

data class PoliceModel(
    var policeStationName : String = "",
    var policeStationLocation : GeoPoint?=null,
    var policeStationPhone : String = ""
)
