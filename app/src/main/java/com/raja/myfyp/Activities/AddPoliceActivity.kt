package com.raja.myfyp.Activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raja.myfyp.ModelClasses.PoliceDataClass
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityAddPoliceBinding
import com.raja.myfyp.databinding.CustomDialogAddBinding

class AddPoliceActivity : BaseActivity() {
    lateinit var binding: ActivityAddPoliceBinding
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPoliceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("PoliceData")


        binding.submitBtn.setOnClickListener {
            val name = binding.nameET.text.toString()
            val password = binding.passwordET.text.toString()
            val address = binding.addressET.text.toString()
            val email = binding.emailET.text.toString()
            val rank = binding.rankET.text.toString()

            if(name.isNullOrEmpty()){
                binding.nameET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if(password.isNullOrEmpty()){
                binding.nameET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if(address.isNullOrEmpty()){
                binding.nameET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if(email.isNullOrEmpty()){
                binding.nameET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if(rank.isNullOrEmpty()){
                binding.nameET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }

            registerPolice(email,password,name,address,rank)

        }

    }

    fun registerPolice(email: String, password: String, name: String, address: String, rank: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result?.user
                    val uid = user?.uid
                    val police = PoliceDataClass(
                        id = uid,
                        name = name,
                        password = password,
                        address = address,
                        email = email,
                        rank = rank
                    )
                    // Save police data to Realtime Database
                    database.child("${police.id}").setValue(police)
                        .addOnSuccessListener {
                            showSuccessDialog()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(
                                this@AddPoliceActivity,
                                exception.message, Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        this@AddPoliceActivity,
                        it.exception.toString(), Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun showSuccessDialog(){
        val dialog_binding = CustomDialogAddBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(dialog_binding.root)

        val window: Window = dialog.window!!
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setGravity(Gravity.CENTER)

        dialog.show()
        Handler().postDelayed({
            dialog.dismiss()
            finish()
        }, 3000)
    }


}