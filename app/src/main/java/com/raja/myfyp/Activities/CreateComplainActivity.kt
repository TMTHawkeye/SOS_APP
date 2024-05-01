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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raja.myfyp.ModelClasses.Complain
import com.raja.myfyp.ModelClasses.PoliceDataClass
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityCreateComplainBinding
import com.raja.myfyp.databinding.CustomDialogAddBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateComplainActivity : BaseActivity() {
    lateinit var binding: ActivityCreateComplainBinding
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateComplainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("ComplainData")

        binding.cardCreateId.setOnClickListener {
            val name = binding.nameET.text.toString()
            val email = binding.emailET.text.toString()
            val addressET = binding.addressET.text.toString()
            val complain = binding.complainET.text.toString()

            if(name.isEmpty()){
                binding.nameET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if(email.isEmpty()){
                binding.emailET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if(addressET.isEmpty()){
                binding.addressET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if(complain.isEmpty()){
                binding.complainET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            val newcomplainRef = database.push().key
            val currentDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

            val complainData = Complain(
                complainId =newcomplainRef,
                userId = FirebaseAuth.getInstance().currentUser?.uid,
                userName = name,
                userEmail = email,
                status = "Under Process",
                date = currentDate,
                userAddress = addressET,
                complainDesc = complain
            )
            createComplain(complainData)
        }

        binding.backBtnImg.setOnClickListener {
            finish()
        }



    }

    fun createComplain(
       complainData : Complain
    ) {
        database.child("${complainData.complainId}").setValue(complainData)
            .addOnSuccessListener {
                showSuccessDialog()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this@CreateComplainActivity,
                    exception.message, Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun showSuccessDialog() {
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