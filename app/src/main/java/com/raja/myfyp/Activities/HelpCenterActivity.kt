package com.raja.myfyp.Activities

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityHelpCenterBinding

class HelpCenterActivity : BaseActivity() {
    lateinit var binding :ActivityHelpCenterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityHelpCenterBinding.inflate(layoutInflater)
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