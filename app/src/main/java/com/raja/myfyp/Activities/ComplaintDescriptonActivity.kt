package com.raja.myfyp.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.ModelClasses.Complain
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityComplaintDescriptonBinding

class ComplaintDescriptonActivity : BaseActivity() {
    lateinit var binding: ActivityComplaintDescriptonBinding
    var complain: Complain? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComplaintDescriptonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        complain = intent?.getSerializableExtra("complain") as? Complain
        complain?.let {
            binding.nameId.text = it.userName
            binding.complainId.text = it.complainDesc
            binding.emailId.text = it.userEmail
            binding.locationId.text = it.userAddress
        }

        binding.backBtnImg.setOnClickListener {
            finish()
        }
    }
}