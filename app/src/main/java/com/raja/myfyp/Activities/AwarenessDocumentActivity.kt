package com.raja.myfyp.Activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityAwarenessDocumentBinding

class AwarenessDocumentActivity : BaseActivity() {
    lateinit var binding : ActivityAwarenessDocumentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityAwarenessDocumentBinding.inflate(layoutInflater)
         setContentView(binding.root)
        binding.backBtnImg.setOnClickListener {
            finish()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

    }
}