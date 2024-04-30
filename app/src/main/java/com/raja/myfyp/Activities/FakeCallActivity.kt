package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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

        val storedFakeCallTimeMinutes = Paper.book().read<Int>("fakeCallTime", -1)
        if(storedFakeCallTimeMinutes!=null) {
            if (storedFakeCallTimeMinutes != -1) {
                val storedFakeCallHours = storedFakeCallTimeMinutes / 60
                val storedFakeCallMinutes = storedFakeCallTimeMinutes % 60

                binding.hourPicker.value = storedFakeCallHours
                binding.minutePicker.value = storedFakeCallMinutes
            }
        }
        val storedcallerName = Paper.book().read<String>("callerName", null)
        storedcallerName?.let {
            binding.editTextname.setText(it)
        }

        binding.updateId.setOnClickListener {
            if(!binding.editTextname.text.isNullOrEmpty()) {
                val fakeCallTimeMinutes = (binding.hourPicker.value * 60) + binding.minutePicker.value
                Paper.book().write("fakeCallTime", fakeCallTimeMinutes)
                Paper.book().write("callerName",binding.editTextname.text.toString())
                Toast.makeText(this@FakeCallActivity, "Caller Name Updated Successfully!", Toast.LENGTH_SHORT).show()
                startService(Intent(this, CallService::class.java))
                finish()
            }
            else{
                Toast.makeText(this@FakeCallActivity, "Name cannot be null!", Toast.LENGTH_SHORT).show()
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
}