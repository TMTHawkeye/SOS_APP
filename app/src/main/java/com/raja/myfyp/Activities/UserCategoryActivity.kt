package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityUserCategoryBinding
import io.paperdb.Paper

class UserCategoryActivity : BaseActivity() {
    lateinit var binding: ActivityUserCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userLoginId.setOnClickListener {
            startActivity(Intent(this@UserCategoryActivity, OTPActivity::class.java))
        }

        binding.adminLoginId.setOnClickListener {
            startActivity(Intent(this@UserCategoryActivity, admin_login_activity::class.java))
        }

        binding.policeLoginId.setOnClickListener {
            startActivity(Intent(this@UserCategoryActivity, PoliceLoginActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        val userType: String = Paper.book().read<String>("USERTYPE") ?: ""
        userType?.let {
            if (userType.equals("ADMIN")) {
                startActivity(Intent(this@UserCategoryActivity, AdminDashboardActivity::class.java))
            } else if (userType.equals("USER")) {
                startActivity(Intent(this@UserCategoryActivity, MainActivity::class.java))
            } else if (userType.equals("POLICE")) {
                startActivity(
                    Intent(
                        this@UserCategoryActivity,
                        PoliceDashboardActivity::class.java
                    )
                )
            }
        }

    }

}