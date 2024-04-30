package com.raja.myfyp.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.raja.myfyp.databinding.ActivityOtpverificationBinding
import java.util.concurrent.TimeUnit


class OTPVerificationActivity : BaseActivity() {
    lateinit var binding: ActivityOtpverificationBinding
     lateinit var auth: FirebaseAuth
    var pd: ProgressDialog? = null
    private var verificationId = ""
     var phoneNumber :String?= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpverificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
         pd = ProgressDialog(this@OTPVerificationActivity);
        phoneNumber = intent?.getStringExtra("number")
        phoneNumber?.let {
            sendVerificationCode(it)
        }



        binding.submitBtn.setOnClickListener {
            if (binding.editTextPhone.text.toString().isEmpty()) {
                Toast.makeText(
                    this@OTPVerificationActivity,
                    "OTP cannot be empty!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.editTextPhone.text.toString().length != 6) {
                Toast.makeText(
                    this@OTPVerificationActivity,
                    "Invalid OTP length!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                pd?.setMessage("Signing In! Please Wait...");
                pd?.show();
                verifyCode(binding.editTextPhone.text.toString())

            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }

        })


    }


    private fun sendVerificationCode(phoneNumber: String?) {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber ?: "03345515964") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            var code: String? = credential.smsCode
            verifyCode(code)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(
                this@OTPVerificationActivity,
                "Verification Failed! ${e.message}",
                Toast.LENGTH_LONG
            )
                .show()
            Log.d("TAG_verificationStatus", "onVerificationFailed: ${e.message}")

        }

        override fun onCodeSent(
            s: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            super.onCodeSent(s, token)
            verificationId = s
            binding.editTextPhone.isEnabled=true

        }
    }

    fun verifyCode(code: String?) {
        var credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential(verificationId, code ?: "")
        signInbyCredentials(credential)
    }

    fun signInbyCredentials(credential: PhoneAuthCredential) {
        var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    this@OTPVerificationActivity,
                    "Login Successfull!",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(Intent(this@OTPVerificationActivity, UserProfileActivity::class.java))
            } else {
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    Log.d("TAG", "signInbyCredentials: ${task.exception?.message}")
                }
                Toast.makeText(this@OTPVerificationActivity, "Invalid OTP", Toast.LENGTH_SHORT)
                    .show()
                pd?.dismiss()
                // Update UI
            }
        }

    }

}