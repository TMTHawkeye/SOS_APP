package com.raja.myfyp

import android.content.Context
import android.location.LocationManager
import io.paperdb.Paper

const val OVERLAY_PERMISSION_REQUEST_CODE = 123
const val PREF_NAME_LANGUAGE = "LanguagePreferences"
const val LANGUAGE_POSITION_KEY = "LANG_POS"


fun isFirstTimeLaunch(): Boolean {
    val navigate_status = Paper.book().read<Boolean>("NAV_STATE", true)
    return navigate_status!!
}

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

/*
fun clearCredentials(ctxt:Context) {
    Paper.book().destroy()
    Paper.init(ctxt)
}*/
