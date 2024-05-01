package com.raja.myfyp.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityAppPermissionBinding
import io.paperdb.Paper

class AppPermissionActivity : BaseActivity() {
    lateinit var binding: ActivityAppPermissionBinding
    private val PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(hasOverlayPermission()) {
            binding.radioOverlayBtn.isChecked = true
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ){
            binding.radioPowerBtn.isChecked=true
        }
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ){
            binding.radioLocationBtn.isChecked=true
        }

        if(binding.radioOverlayBtn.isChecked&&binding.radioPowerBtn.isChecked&&binding.radioLocationBtn.isChecked){
            Paper.book().write("NAV_STATE${FirebaseAuth.getInstance().currentUser?.uid}", false)
            startActivity(Intent(this@AppPermissionActivity, MainActivity::class.java))
        }

        binding.allowBtn.setOnClickListener {
            val permissionsToRequest = mutableListOf<String>()

            // Check if location permission is granted, if not add it to the list of permissions to request
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }

            // Check if SMS permission is granted, if not add it to the list of permissions to request
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.SEND_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionsToRequest.add(android.Manifest.permission.SEND_SMS)
            }

            // Check if overlay permission is granted, if not add it to the list of permissions to request
            if (!hasOverlayPermission()) {
                permissionsToRequest.add(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            }

            // Request all permissions at once
            if (permissionsToRequest.isEmpty()) {
                // All permissions are already granted, proceed to main activity
                Paper.book().write("NAV_STATE${FirebaseAuth.getInstance().currentUser?.uid}", false)
                startActivity(Intent(this@AppPermissionActivity, MainActivity::class.java))
            } else {
                // Request permissions
                ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toTypedArray(),
                    PERMISSION_REQUEST_CODE
                )
            }
        }

        binding.denyBtn.setOnClickListener {
            Paper.book().write("NAV_STATE${FirebaseAuth.getInstance().currentUser?.uid}", false)
            startActivity(Intent(this@AppPermissionActivity, MainActivity::class.java))
        }

        binding.backBtnImg.setOnClickListener {
            finish()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            var allPermissionsGranted = true

            // Check if all requested permissions are granted
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false
                    break
                }
            }

            if (allPermissionsGranted) {
                // All permissions are granted, proceed to main activity
                Paper.book().write("NAV_STATE${FirebaseAuth.getInstance().currentUser?.uid}", false)
                startActivity(Intent(this@AppPermissionActivity, MainActivity::class.java))
            } else {
                // Handle case where permissions are denied
//                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
                Paper.book().write("NAV_STATE${FirebaseAuth.getInstance().currentUser?.uid}", false)
                startActivity(Intent(this@AppPermissionActivity, MainActivity::class.java))
            }
        }
    }

 }
