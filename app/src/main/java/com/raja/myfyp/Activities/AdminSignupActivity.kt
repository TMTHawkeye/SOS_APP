package com.raja.myfyp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityAdminSignupBinding

class AdminSignupActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminSignupBinding
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.backBtnImg.setOnClickListener {
            finish()
        }


        binding.submitBtn.setOnClickListener {
            val email = binding.adminNameET.text.toString()
            val password = binding.adminPassET.text.toString()
            val confirm = binding.adminConfirmPassET.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty()) {
                if (password == confirm) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                val intent = Intent(
                                    this@AdminSignupActivity,
                                    admin_login_activity::class.java
                                )
                                startActivity(intent)

                            } else {
                                Toast.makeText(
                                    this@AdminSignupActivity,
                                    it.exception.toString(), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(
                        this@AdminSignupActivity,
                        getString(R.string.password_does_not_match), Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@AdminSignupActivity,
                    getString(R.string.field_cannot_be_empty), Toast.LENGTH_SHORT
                ).show()
            }
        }


    }
}