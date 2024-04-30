package com.raja.myfyp.Activities

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.ModelClasses.UserData
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityChangePinBinding
import io.paperdb.Paper

class ChangePinActivity : BaseActivity() {
    lateinit var binding: ActivityChangePinBinding
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePinBinding.inflate(layoutInflater)
        setContentView(binding.root)

         binding.updateBtn.setOnClickListener {
             val presentPin = Paper.book().read<String>("USER_PIN")
              val newPin = binding.editTextnewPin.text.toString()
             val confirmPin = binding.editTextconfirm.text.toString()

             if (presentPin.isNullOrEmpty() || newPin.isEmpty() || confirmPin.isEmpty()) {
                 Toast.makeText(this@ChangePinActivity, "Please fill in all fields!", Toast.LENGTH_SHORT).show()
             } else {
                 if (presentPin == newPin && presentPin == confirmPin) {
                     Toast.makeText(this@ChangePinActivity, "PIN should be unique!", Toast.LENGTH_SHORT).show()
                 } else {
                     if(presentPin.equals(binding.editTextpresentPin.text.toString())) {
                         if (newPin == confirmPin) {
                             Paper.book().write("USER_PIN", newPin)
                             Toast.makeText(
                                 this@ChangePinActivity,
                                 "PIN updated successfully!",
                                 Toast.LENGTH_SHORT
                             ).show()
                             finish()
                         } else {
                             Toast.makeText(
                                 this@ChangePinActivity,
                                 "New PIN and Confirm PIN do not match!",
                                 Toast.LENGTH_SHORT
                             ).show()
                         }
                     }
                     else{
                         Toast.makeText(this@ChangePinActivity, "Present PIN is Invalid!", Toast.LENGTH_SHORT).show()
                     }
                 }
             }
         }

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