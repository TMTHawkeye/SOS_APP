package com.raja.myfyp.Services

// PowerButtonService.kt
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.raja.myfyp.R
import java.util.Timer
import java.util.TimerTask

class PowerButtonService : Service() {
    private val FOREGROUND_SERVICE_ID = 101
    private val CHANNEL_ID = "SOS Application"

    private var powerButtonPressCount = 0
    private var lastPowerButtonPressTime: Long = 0

   /* private val powerButtonReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_SCREEN_OFF ){
                detectPowerButtonPress()
            }
        }
    }*/

    private val powerButtonReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                Intent.ACTION_SCREEN_OFF, Intent.ACTION_SCREEN_ON -> {
                    detectPowerButtonPress()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "SOS Service is Started", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val drawableIcon : Int = R.drawable.ic_launcher_foreground ?: R.drawable.ic_launcher_foreground
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SOS App.")
            .setContentText("Power Button SOS Service Running...")
            .setSmallIcon(drawableIcon)
            .build()

        startForeground(FOREGROUND_SERVICE_ID, notification)

        registerPowerButtonReceiver()


        val timer = Timer()
        val handler = Handler(Looper.getMainLooper())
        val doAsynchronousTask = object : TimerTask() {
            override fun run() {
                handler.post {
                }
            }
        }
        timer.schedule(doAsynchronousTask, 5000L, 5000L)
    }



    override fun onDestroy() {
        super.onDestroy()
        unregisterPowerButtonReceiver()
        stopForeground(true)

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

 /*   private fun registerPowerButtonReceiver() {
        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF )
        registerReceiver(powerButtonReceiver, filter)
    }*/

    private fun registerPowerButtonReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        registerReceiver(powerButtonReceiver, filter)
    }


    private fun unregisterPowerButtonReceiver() {
        unregisterReceiver(powerButtonReceiver)
    }

    private fun detectPowerButtonPress() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastPowerButtonPressTime > 1000) {
            powerButtonPressCount = 1
        } else {
            powerButtonPressCount++
            if (powerButtonPressCount == 3) {
                startSosSendingService()
                powerButtonPressCount = 0
            }
        }
        lastPowerButtonPressTime = currentTime
    }

    private fun startSosSendingService() {
        val intent = Intent(applicationContext, SOSService::class.java)
        startService(intent)
    }
}
