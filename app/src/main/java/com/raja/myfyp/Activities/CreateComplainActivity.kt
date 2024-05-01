package com.raja.myfyp.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.ModelClasses.Complain
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityCreateComplainBinding

class CreateComplainActivity : BaseActivity() {
    lateinit var binding: ActivityCreateComplainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateComplainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

            val complainData = Complain(
                userName = name,
                userEmail = email,
                userAddress = addressET,
                complainDesc = complain
            )
        }


    }
}