package com.raja.myfyp.Activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.raja.myfyp.Interfaces.PinConfirmationCallback
import com.raja.myfyp.R
import com.raja.myfyp.databinding.ActivityCancelSosactivityBinding
import io.paperdb.Paper

class CancelSOSActivity : BaseActivity() {
    lateinit var binding : ActivityCancelSosactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCancelSosactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pin=Paper.book().read<String>("USER_PIN","")

        binding.submitBtn.setOnClickListener {
            if(binding.pinId.text.toString().equals(pin)){
                cancellationListener?.pinConfirmed(true)
                finish()
            }
            else{
                Toast.makeText(this@CancelSOSActivity, "Incorrect PIN!", Toast.LENGTH_SHORT).show()
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

//    fun setCancellationListener(listener: PinConfirmationCallback) {
//        cancellationListener = listener
//    }

    companion object{
        private var cancellationListener: PinConfirmationCallback? = null
        fun getInstance(listner : PinConfirmationCallback) : CancelSOSActivity{
            cancellationListener=listner
            return CancelSOSActivity()
        }
    }
}