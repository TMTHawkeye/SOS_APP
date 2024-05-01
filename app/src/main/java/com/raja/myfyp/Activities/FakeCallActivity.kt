package com.raja.myfyp.Activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.OVERLAY_PERMISSION_REQUEST_CODE
import com.raja.myfyp.R
import com.raja.myfyp.Services.CallService
import com.raja.myfyp.databinding.ActivityFakeCallBinding
import io.paperdb.Paper

class FakeCallActivity : BaseActivity() {
    lateinit var binding: ActivityFakeCallBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFakeCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hourPicker.minValue = 0
        binding.hourPicker.maxValue = 23
        binding.hourPicker.wrapSelectorWheel = true

        binding.minutePicker.minValue = 0
        binding.minutePicker.maxValue = 59
        binding.minutePicker.wrapSelectorWheel = true

        val storedFakeCallTime = Paper.book().read<String>("fakeCallTime")
        if (!storedFakeCallTime.isNullOrEmpty()) {
            val timeParts = storedFakeCallTime.split(":")
            if (timeParts.size >= 2) {
                val storedFakeCallHours = timeParts[0].toInt()
                val storedFakeCallMinutes = timeParts[1].toInt()

                 binding.hourPicker.value = storedFakeCallHours
                binding.minutePicker.value = storedFakeCallMinutes
            }
        }


        val storedcallerName = Paper.book().read<String>("callerName", null)
        storedcallerName?.let {
            binding.editTextname.setText(it)
        }

        binding.updateId.setOnClickListener {
            if (!hasOverlayPermission()) {
                requestOverlayPermission()
            } else {
                if (!binding.editTextname.text.isNullOrEmpty()) {
                    val fakeCallTimeMinutes =
                        (binding.hourPicker.value * 60) + binding.minutePicker.value
//               Paper.book().write("fakeCallTime", fakeCallTimeMinutes

                    val totalMinutes = fakeCallTimeMinutes
                    val hours = totalMinutes / 60
                    val minutes = totalMinutes % 60
                    val seconds = 0

                    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)

                    Paper.book().write("fakeCallTime", timeString)

                    Paper.book().write("callerName", binding.editTextname.text.toString())
                    Toast.makeText(
                        this@FakeCallActivity,
                        "Caller Name Updated Successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    startService(Intent(this, CallService::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this@FakeCallActivity,
                        "Name cannot be null!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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

    private fun requestOverlayPermission() {
        if (!hasOverlayPermission()) {
            val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE)
        }
    }

    private fun hasOverlayPermission(): Boolean {
        return Settings.canDrawOverlays(this)
    }

}