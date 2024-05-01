package com.raja.myfyp.Services

import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.IBinder
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.raja.myfyp.ModelClasses.Contact
import com.raja.myfyp.ModelClasses.PoliceDataClass
import io.paperdb.Paper
import java.net.URLEncoder
import java.util.jar.Manifest

class SOSService : Service() {
    private lateinit var database: DatabaseReference

    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 1
    private val MY_PERMISSIONS_REQUEST_LOCATION = 2

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
    var contacts : ArrayList<Contact>?=null


    override fun onCreate() {
        super.onCreate()
        Paper.init(this@SOSService)
        database = FirebaseDatabase.getInstance().reference.child("PoliceData")
        getAllPolice {
            contacts = getSelectedContacts(it)
        }

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
//                    val contacts = getSelectedContacts()
                    contacts?.let {
                        // Send SOS message to each contact
                        for (contact in it) {
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

//    private fun getSelectedContacts(): ArrayList<Contact>? {
//        // Retrieve emergency contacts from storage (you can implement your logic here)
//        return Paper.book().read("EMERGENCY_CONTACTS", ArrayList<Contact>())
//    }

    private fun getSelectedContacts(policeDataClasses: ArrayList<PoliceDataClass>?): ArrayList<Contact>? {
        var policeContacts = getAllPoliceContacts(policeDataClasses)

        var listOfCOntacts = Paper.book().read("EMERGENCY_CONTACTS${FirebaseAuth.getInstance().currentUser?.uid}", ArrayList<Contact>())
        Log.d("TAG", "getSelectedContacts: $listOfCOntacts")
        policeContacts?.let { listOfCOntacts?.addAll(it) }
        return listOfCOntacts
    }


    fun getAllPoliceContacts(policeDataClasses: ArrayList<PoliceDataClass>?): ArrayList<Contact>?{
        val allContactsPolice = ArrayList<Contact>()
//        getAllPolice { policeList ->
//            policeList?.let { list ->
//
//            }
//        }
        policeDataClasses?.let {
            for (police in it) {
                allContactsPolice.add(Contact(police.name, police.phone))
            }
        }
        return allContactsPolice
    }

    fun getAllPolice(callback: (ArrayList<PoliceDataClass>?) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val policeList = ArrayList<PoliceDataClass>()
                for (policeSnapshot in snapshot.children) {
                    val police = policeSnapshot.getValue(PoliceDataClass::class.java)
                    police?.let { policeList.add(it) }
                }
                callback(policeList)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }

}