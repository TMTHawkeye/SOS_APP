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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.raja.myfyp.ModelClasses.PoliceDataClass
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityAddPoliceBinding
import com.raja.myfyp.databinding.CustomDialogAddBinding

class AddPoliceActivity : BaseActivity() {
    lateinit var binding: ActivityAddPoliceBinding
    private lateinit var database: DatabaseReference
    var policeId: PoliceDataClass? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPoliceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance().getReference("PoliceData")

        policeId = intent?.getSerializableExtra("policeItem") as? PoliceDataClass

        policeId?.let {
            binding.nameET.setText(it.name)
            binding.passwordET.setText(it.password)
            binding.addressET.setText(it.address)
            binding.emailET.setText(it.email)
            binding.rankET.setText(it.rank)
            binding.phoneET.setText(it.phone)
            binding.textView2.text = getString(R.string.update)
        }



        binding.submitBtn.setOnClickListener {
            val name = binding.nameET.text.toString()
            val password = binding.passwordET.text.toString()
            val address = binding.addressET.text.toString()
            val email = binding.emailET.text.toString()
            val rank = binding.rankET.text.toString()
            val phone = binding.phoneET.text.toString()

            if (name.isNullOrEmpty()) {
                binding.nameET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if (password.isNullOrEmpty()) {
                binding.passwordET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if (address.isNullOrEmpty()) {
                binding.addressET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if (email.isNullOrEmpty()) {
                binding.emailET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if (rank.isNullOrEmpty()) {
                binding.rankET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }
            if (phone.isNullOrEmpty()) {
                binding.phoneET.setError(getString(R.string.field_cannot_be_empty))
                return@setOnClickListener
            }

            if (policeId != null) {
                policeId?.let {
                    val police = PoliceDataClass(
                        id = it.id,
                        name = name,
                        password = password,
                        address = address,
                        email = email,
                        rank = rank,
                        phone = phone
                    )

                    editPolice(it.id, police) {
                        if (it) {
                            Toast.makeText(
                                this@AddPoliceActivity,
                                getString(R.string.updated_successfully),
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        } else {
                            Toast.makeText(
                                this@AddPoliceActivity,
                                getString(R.string.something_went_wrong),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                registerPolice(email, password, name, address, rank,phone)

            }

        }

        binding.backBtnImg.setOnClickListener {
            finish()
        }

    }

    fun registerPolice(
        email: String,
        password: String,
        name: String,
        address: String,
        rank: String,
        phone: String
    ) {
        val newPoliceRef = database.push().key

        val police = PoliceDataClass(
            id = newPoliceRef,
            name = name,
            password = password,
            address = address,
            email = email,
            rank = rank,
            phone = phone
        )

        // Save police data to Realtime Database
        database.child("${newPoliceRef}").setValue(police)
            .addOnSuccessListener {
                showSuccessDialog()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this@AddPoliceActivity,
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

    fun editPolice(policeId: String?, newData: PoliceDataClass?, callback: (Boolean) -> Unit) {
        database.child("$policeId").setValue(newData)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { exception ->
                callback(false)
            }
    }


}