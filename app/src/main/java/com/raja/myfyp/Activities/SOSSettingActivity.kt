package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivitySossettingBinding
import io.paperdb.Paper

class SOSSettingActivity : BaseActivity() {
    lateinit var binding: ActivitySossettingBinding
    var intentFrom : String ?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySossettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val powerState = Paper.book().read<Boolean>("POWER_SERVICE${FirebaseAuth.getInstance().currentUser?.uid}", false)
        val shakeState = Paper.book().read<Boolean>("SHAKE_SERVICE${FirebaseAuth.getInstance().currentUser?.uid}", false)

        intentFrom = intent?.getStringExtra("intentFrom")

        binding.radioPowerBtn.isChecked = powerState ?: false
        binding.radioShakeBtn.isChecked = shakeState ?: false

//        binding.radioPowerBtn.setOnClickListener {
//            // Toggle the checked state
//            binding.radioPowerBtn.isChecked = !binding.radioPowerBtn.isChecked
//        }
//
//        binding.radioShakeBtn.setOnClickListener {
//            // Toggle the checked state
//            binding.radioShakeBtn.isChecked = !binding.radioShakeBtn.isChecked
//        }

//        binding.radioPowerBtn.setOnClickListener {
//            // Toggle the checked state
//            binding.radioPowerBtn.isChecked = !binding.radioPowerBtn.isChecked
//            // Save the state to Paper
//            Paper.book().write("POWER_SERVICE", binding.radioPowerBtn.isChecked)
//        }
//
//        binding.radioShakeBtn.setOnClickListener {
//            // Toggle the checked state
//            binding.radioShakeBtn.isChecked = !binding.radioShakeBtn.isChecked
//            // Save the state to Paper
//            Paper.book().write("SHAKE_SERVICE", binding.radioShakeBtn.isChecked)
//        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            // Handle the checkedId to uncheck the other radio button
            when (checkedId) {
                R.id.radio_power_btn -> {
                    // If power button is checked, uncheck shake button
                    binding.radioShakeBtn.isChecked = false
                    Paper.book().write("SHAKE_SERVICE${FirebaseAuth.getInstance().currentUser?.uid}",false)
                    Paper.book().write("POWER_SERVICE${FirebaseAuth.getInstance().currentUser?.uid}",true)
                }

                R.id.radio_shake_btn -> {
                    // If shake button is checked, uncheck power button
                    binding.radioPowerBtn.isChecked = false
                    Paper.book().write("POWER_SERVICE${FirebaseAuth.getInstance().currentUser?.uid}",false)
                    Paper.book().write("SHAKE_SERVICE${FirebaseAuth.getInstance().currentUser?.uid}",true)

                }
            }
        }


        binding.updateBtn.setOnClickListener {
            Paper.book().write<Boolean>("SOSSettingsEnabled${FirebaseAuth.getInstance().currentUser?.uid}",true)
//            Paper.book().write("POWER_SERVICE", binding.radioPowerBtn.isChecked)
//
//            Paper.book().write("SHAKE_SERVICE", binding.radioShakeBtn.isChecked)
            if (intentFrom.equals("fromMain")) {
                finish()
            } else {
                startActivity(Intent(this@SOSSettingActivity, PinSettingsActivity::class.java))
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

    override fun onStart() {
        super.onStart()
        if(intentFrom!="fromMain") {
            val sosSettingsStatus = Paper.book().read<Boolean>(
                "SOSSettingsEnabled${FirebaseAuth.getInstance().currentUser?.uid}",
                false
            ) ?: false
            if (sosSettingsStatus) {
                startActivity(Intent(this@SOSSettingActivity, PinSettingsActivity::class.java))
            }
        }
    }
}