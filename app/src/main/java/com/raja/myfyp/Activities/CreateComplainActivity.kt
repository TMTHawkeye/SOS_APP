package com.raja.myfyp.Activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityCreateComplainBinding

class CreateComplainActivity : BaseActivity() {
    lateinit var binding: ActivityCreateComplainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateComplainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}