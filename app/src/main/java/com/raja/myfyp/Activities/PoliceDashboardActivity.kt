package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.R
//import com.raja.myfyp.clearCredentials
import com.raja.myfyp.databinding.ActivityPoliceDashboardBinding
import io.paperdb.Paper

class PoliceDashboardActivity : AppCompatActivity() {
    lateinit var binding : ActivityPoliceDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPoliceDashboardBinding.inflate(layoutInflater)
         setContentView(binding.root)
        Paper.book().write("USERTYPE" , "POLICE")


         val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Paper.book().write("USERTYPE" , "")
//                clearCredentials(this@PoliceDashboardActivity)
                startActivity(Intent(this@PoliceDashboardActivity,UserCategoryActivity::class.java))
                finish()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)


    }
}