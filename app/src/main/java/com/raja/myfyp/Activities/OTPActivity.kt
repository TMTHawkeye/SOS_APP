package com.raja.myfyp.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityOtpactivityBinding
import io.paperdb.Paper
import java.util.concurrent.TimeUnit

class OTPActivity : BaseActivity() {
    lateinit var otpBinding: ActivityOtpactivityBinding

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth

    // [END declare_auth]
    var pd: ProgressDialog? = null

    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpBinding = ActivityOtpactivityBinding.inflate(layoutInflater)
        val view: View = otpBinding.getRoot()
        setContentView(view)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        pd = ProgressDialog(this@OTPActivity);

        auth = Firebase.auth

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:$credential")
                pd?.dismiss()
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e)
                Toast.makeText(this@OTPActivity, "${e.message}", Toast.LENGTH_LONG).show()
                pd?.dismiss()

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                Log.d(TAG, "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
                pd?.dismiss()
                val intent = Intent(this@OTPActivity, OTPVerificationActivity::class.java)
                intent.putExtra("storedVerificationId", storedVerificationId)
                startActivity(intent)
            }
        }
        // [END phone_auth_callbacks]


        otpBinding.ccp.registerCarrierNumberEditText(otpBinding.editTextPhone)
//        countryCodesAdapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_spinner_item,
//            mutableListOf()
//        )
//        countryCodesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        otpBinding.codesSpinnerId.adapter = countryCodesAdapter

//        userViewModel.getCountryCodes { spinnerList ->
//            countryCodesAdapter.addAll(spinnerList)
//
//            val defaultIndex = spinnerList.indexOf("+92")
//            if (defaultIndex != -1) {
//                otpBinding.codesSpinnerId.setSelection(defaultIndex)
//            }
//        }

//        otpBinding.codesSpinnerId.onItemSelectedListener =
//            object : AdapterView.OnItemSelectedListener {
//                override fun onItemSelected(
//                    parent: AdapterView<*>?,
//                    view: View?,
//                    position: Int,
//                    id: Long
//                ) {
//                    selectedCountryCode = countryCodesAdapter.getItem(position) ?: "+92"
//                }
//
//                override fun onNothingSelected(parent: AdapterView<*>?) {
//                }
//            }


        otpBinding.submitBtn.setOnClickListener { v: View? ->
            if(!otpBinding.editTextPhone.text.toString().isNullOrEmpty()) {
                pd?.setMessage("Confirming Status! Please Wait...");
                pd?.show();
                startPhoneNumberVerification(otpBinding.ccp.fullNumberWithPlus.replace(" ", ""))
            }
            else{
                Toast.makeText(this@OTPActivity, "Phone Number cannot be empty!", Toast.LENGTH_SHORT).show()
            }
            //            val phoneNumber = otpBinding.editTextPhone.text.toString().trim()
//            startActivity(
//                Intent(this@OTPActivity, OTPVerificationActivity::class.java).putExtra(
//                    "number",
//                    otpBinding.ccp.fullNumberWithPlus.replace(" ", "")
//                )
//            )

        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }

        })

    }



    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }


    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    val intent = Intent(this@OTPActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]

    private fun updateUI(user: FirebaseUser? = auth.currentUser) {
    }

    companion object {
        private const val TAG = "PhoneAuthActivity"
    }


}