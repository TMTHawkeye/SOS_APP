package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.Fragments.HomeFragment
import com.raja.myfyp.R
//import com.raja.myfyp.clearCredentials
import com.raja.myfyp.databinding.ActivityAdminDashboardBinding
import io.paperdb.Paper

class AdminDashboardActivity : BaseActivity() {
    lateinit var binding : ActivityAdminDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAdminDashboardBinding.inflate(layoutInflater)
         setContentView(binding.root)
        Paper.book().write("USERTYPE" , "ADMIN")

        binding.cardRegisterPolice.setOnClickListener {
            startActivity(Intent(this@AdminDashboardActivity,AddPoliceActivity::class.java))
        }

        binding.cardManagePoliceDetails.setOnClickListener {
            startActivity(Intent(this@AdminDashboardActivity,EditPoliceActivity::class.java))
        }

        // Create a callback for back button press
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Paper.book().write("USERTYPE" , "")
//                clearCredentials(this@AdminDashboardActivity)
                startActivity(Intent(this@AdminDashboardActivity,UserCategoryActivity::class.java))
                finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)

    }
}