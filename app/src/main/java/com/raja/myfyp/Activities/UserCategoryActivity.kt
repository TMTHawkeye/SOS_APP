package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityUserCategoryBinding

class UserCategoryActivity : BaseActivity() {
    lateinit var binding: ActivityUserCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userLoginId.setOnClickListener {
            startActivity(Intent(this@UserCategoryActivity,OTPActivity::class.java))
        }
    }
}