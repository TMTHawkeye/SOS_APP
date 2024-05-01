package com.raja.myfyp.Activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.raja.myfyp.Services.CallService
import com.raja.myfyp.databinding.ActivityCallBinding
import io.paperdb.Paper

class CallActivity : BaseActivity() {
    lateinit var binding: ActivityCallBinding
    lateinit var mediaPlayer: MediaPlayer
    lateinit var handler: Handler
    lateinit var runnable: Runnable
    var number = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Make the activity full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(binding.root)

        Paper.init(this@CallActivity)

        val callerName = Paper.book().read<String>("callerName")
        callerName?.let {
            binding.nameTextView.text=it
        }

        mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        mediaPlayer.setLooping(true)
        mediaPlayer.start()
        number = 0

    }

    fun accept(view: View?) {
        mediaPlayer.stop()
        stopService(Intent(this, CallService::class.java))

        handler = Handler()
        runnable = Runnable {
            if (number < 10) {
                binding.timerTextView.setText("00:0$number")
            } else if (number > 10 && number < 60) {
                binding.timerTextView.setText("00:$number")
            } else if (number > 59 && number % 60 < 10) {
                val second: Int = number / 60
                binding.timerTextView.setText(second.toString() + ":0" + number % 60)
            } else {
                val second: Int = number / 60
                binding.timerTextView.setText(second.toString() + ":" + number % 60)
            }
            number++
            handler.postDelayed(runnable, 1000)
        }
        handler.post(runnable)
    }

    fun decline(view: View?) {
        mediaPlayer.stop()
        stopService(Intent(this, CallService::class.java))
        finish()

    }
}