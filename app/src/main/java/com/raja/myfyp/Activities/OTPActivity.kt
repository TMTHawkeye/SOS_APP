package com.raja.myfyp.Activities

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityOtpactivityBinding
import java.util.concurrent.TimeUnit

class OTPActivity : BaseActivity() {
    lateinit var otpBinding: ActivityOtpactivityBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpBinding = ActivityOtpactivityBinding.inflate(layoutInflater)
        val view: View = otpBinding!!.getRoot()
        setContentView(view)
        val window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)

        auth = FirebaseAuth.getInstance()


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
            //            val phoneNumber = otpBinding.editTextPhone.text.toString().trim()
            startActivity(
                Intent(this@OTPActivity, OTPVerificationActivity::class.java).putExtra(
                    "number",
                    otpBinding.ccp.fullNumberWithPlus.replace(" ", "")
                )
            )

        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }

        })

    }

    override fun onStart() {
        super.onStart()
        var currentUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        if(currentUser!=null){
            startActivity(Intent(this@OTPActivity,MainActivity::class.java))
        }
    }




}