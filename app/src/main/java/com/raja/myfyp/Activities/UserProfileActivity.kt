package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.ModelClasses.UserData
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityUserProfileBinding
import io.paperdb.Paper

class UserProfileActivity : BaseActivity() {
    lateinit var binding: ActivityUserProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitBtn.setOnClickListener {
            val userData = UserData(
                binding.editTextPhone.text.toString(),
                binding.editTextname.text.toString(),
                binding.editTextage.text.toString(),
                binding.editTextaddress.text.toString(),
                binding.editTextemail.text.toString()
            )
            saveDataToPaperDB(userData)
            startActivity(Intent(this@UserProfileActivity,EmergencyContactsActivity::class.java))
            finish()
        }


        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })


    }

    fun saveDataToPaperDB(userData: UserData) {
        Paper.book().write("USER_DATA", userData)

    }
}