package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityAdminLoginBinding

class admin_login_activity : BaseActivity() {
    lateinit var binding: ActivityAdminLoginBinding
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.submitBtn.setOnClickListener {
            val email = binding.adminNameET.text.toString()
            val password = binding.adminPassET.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(
                                this@admin_login_activity,
                                AdminDashboardActivity::class.java
                            )
                            startActivity(intent)

                        } else {
                            Toast.makeText(
                                this@admin_login_activity,
                                it.exception.toString(), Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
            } else {
                Toast.makeText(
                    this@admin_login_activity,
                    getString(R.string.field_cannot_be_empty), Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.signupId.setOnClickListener {
            startActivity(Intent(this@admin_login_activity,AdminSignupActivity::class.java))
        }


    }


}