package com.raja.myfyp.Activities

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieDrawable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.raja.myfyp.Interfaces.PinConfirmationCallback
import com.raja.myfyp.ModelClasses.Contact
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivitySoSactivityBinding
import com.raja.myfyp.databinding.CustomDialogSosAlertBinding
import com.raja.myfyp.isLocationEnabled
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URLEncoder
import kotlin.coroutines.CoroutineContext

class SoSActivity : BaseActivity(), CoroutineScope, PinConfirmationCallback {
    lateinit var sosbinding: ActivitySoSactivityBinding
    private var coroutineJob: Job = Job()
    val coroutineScope = CoroutineScope(Dispatchers.Main)
    private val MY_PERMISSIONS_REQUEST_SEND_SMS = 1
    private val MY_PERMISSIONS_REQUEST_LOCATION = 2
    private var remainingTime: Long = 0 // Variable to store remaining time
    var pinConfirmed = false


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + coroutineJob


    private var longitude: Double? = 0.0
    private var latitude: Double? = 0.0

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 30
        fastestInterval = 10
        priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        maxWaitTime = 60
    }
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val locationList = locationResult.locations
            if (locationList.isNotEmpty()) {
                val location = locationList.last()
                latitude = location.latitude
                longitude = location.longitude
            }
        }
    }

    private var countDownTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sosbinding = ActivitySoSactivityBinding.inflate(layoutInflater)
        val view: View = sosbinding.getRoot()
        setContentView(view)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        sosbinding.amSafeTv.setOnClickListener {
            val cancelActivity = CancelSOSActivity.getInstance(this)
            val intent = Intent(this@SoSActivity, cancelActivity::class.java)
//            cancelActivity.setCancellationListener(this)
            startActivity(intent)
        }

        coroutineJob = launch {
            sosbinding.lottieBgId.repeatCount = LottieDrawable.INFINITE
            sosbinding.lottieBgId.playAnimation()
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.SEND_SMS),
                MY_PERMISSIONS_REQUEST_SEND_SMS
            )
        }
        if (isLocationEnabled(this@SoSActivity)) {
            countDownTimer = object : CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsRemaining = millisUntilFinished / 1000
                    sosbinding.countDownId.text = secondsRemaining.toString()
                    remainingTime = millisUntilFinished // Save remaining time
                }

                override fun onFinish() {
                    sosbinding.countDownId.text = "SOS"
                    sendSOS()
                }
            }
        }
        else{
            turnOnLocation()
        }

        countDownTimer?.start()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(
                    this@SoSActivity,
                    "Cannot force stop SOS for security purpose!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineJob?.let {
            it.cancel()
        }
    }

    private fun sendSOS() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.SEND_SMS),
                MY_PERMISSIONS_REQUEST_SEND_SMS
            )
        } else {
            checkLocationPermission()
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
//                    val googleMapsUrl = "https://www.google.com/maps?q= ${location.latitude},${location.longitude}"
                    val encodedCoordinates =
                        URLEncoder.encode("${location.latitude},${location.longitude}", "UTF-8")
                    val googleMapsUrl =
                        "https://www.google.com/maps/search/?api=1&query=$encodedCoordinates"

                    val message =
                        "SOS! I need your help! My current location is: $googleMapsUrl"
                    val contacts = getSelectedContacts()
                    contacts?.let {
                        for (contact in contacts) {
                            SmsManager.getDefault()
                                .sendTextMessage(contact.phone, null, message, null, null)
                        }
                        Toast.makeText(this, "SOS message sent!", Toast.LENGTH_SHORT).show()
                        showSOSAlertDialog()
                    }

                } else {
                    turnOnLocation()

                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error getting location: $e", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun getSelectedContacts(): ArrayList<Contact>? {
        var listOfCOntacts = Paper.book().read("EMERGENCY_CONTACTS", ArrayList<Contact>())
        Log.d("TAG", "getSelectedContacts: $listOfCOntacts")
        return listOfCOntacts

        // Implement your logic to retrieve selected contacts from your list
//        return arrayOf("+923345515964")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSIONS_REQUEST_SEND_SMS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation()
                } else {
                    Toast.makeText(this, "SMS permission denied!", Toast.LENGTH_SHORT).show()
                }
            }

            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation()
                } else {
                    Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Needed")
                    .setMessage("This app needs the Location permission, please accept to use location functionality")
                    .setPositiveButton(
                        "OK"
                    ) { _, _ ->
                        //Prompt the user once explanation has been shown
                        requestLocationPermission()
                    }
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermission()
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            MY_PERMISSIONS_REQUEST_LOCATION
        )
    }


    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {

            fusedLocationClient?.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

    }


    private fun turnOnLocation() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(this, "Location is Enabled!", Toast.LENGTH_SHORT).show()

        } else {
            finish()
//             Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
            val settingsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(settingsIntent)
        }
    }

    private fun showSOSAlertDialog() {
        val dialog_binding = CustomDialogSosAlertBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialog_binding.root)

        val window: Window = dialog.window!!
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setGravity(Gravity.CENTER)

        dialog.show()

        Handler().postDelayed({
            dialog.dismiss()
            finish()
        }, 3000)

    }

    override fun pinConfirmed(isConfirmed: Boolean) {
        pinConfirmed = isConfirmed
        if (isConfirmed) {
            finish()
        }

    }

}