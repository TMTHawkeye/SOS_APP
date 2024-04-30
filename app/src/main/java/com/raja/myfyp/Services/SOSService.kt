package com.raja.myfyp.Services

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.raja.myfyp.ModelClasses.Contact
import io.paperdb.Paper
import java.net.URLEncoder
import java.util.jar.Manifest

class SOSService : Service() {

    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 1
    private val MY_PERMISSIONS_REQUEST_LOCATION = 2

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Paper.init(this@SOSService)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sendSOS()
        return START_NOT_STICKY
    }

    private fun sendSOS() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            stopSelf() // Stop the service if SMS permission is not granted
            return
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            stopSelf() // Stop the service if location permission is not granted
            return
        }

        // Retrieve last known location
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Prepare SOS message with location
                    val encodedCoordinates =
                        URLEncoder.encode("${location.latitude},${location.longitude}", "UTF-8")
                    val googleMapsUrl =
                        "https://www.google.com/maps/search/?api=1&query=$encodedCoordinates"
                    val message =
                        "SOS! I need your help! My current location is: $googleMapsUrl"

                    // Retrieve emergency contacts
                    val contacts = getSelectedContacts()
                    contacts?.let {
                        // Send SOS message to each contact
                        for (contact in contacts) {
                            SmsManager.getDefault()
                                .sendTextMessage(contact.phone, null, message, null, null)
                        }
                        Toast.makeText(this, "SOS message sent!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Error getting location", Toast.LENGTH_SHORT).show()
                }
                stopSelf() // Stop the service after sending SOS
            }
    }

    private fun getSelectedContacts(): ArrayList<Contact>? {
        // Retrieve emergency contacts from storage (you can implement your logic here)
        return Paper.book().read("EMERGENCY_CONTACTS", ArrayList<Contact>())
    }
}