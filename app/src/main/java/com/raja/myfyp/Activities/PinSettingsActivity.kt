package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.raja.myfyp.Interfaces.PinConfirmationCallback
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityPinSettingsBinding
import io.paperdb.Paper

class PinSettingsActivity : BaseActivity() {
    lateinit var binding: ActivityPinSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPinSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createBtn.setOnClickListener {
            if(binding.pinId.text.isNotBlank()) {
                Paper.book().write("USER_PIN${FirebaseAuth.getInstance().currentUser?.uid}", binding.pinId.text.toString())
                startActivity(Intent(this@PinSettingsActivity,AppPermissionActivity::class.java))
            }
            else{
                binding.pinId.error="Please Enter 4 digit Pin"
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
        val userPin = Paper.book().read<String>("USER_PIN${FirebaseAuth.getInstance().currentUser?.uid}")
        if(userPin!=null){
            startActivity(Intent(this@PinSettingsActivity,AppPermissionActivity::class.java))
        }
    }


}