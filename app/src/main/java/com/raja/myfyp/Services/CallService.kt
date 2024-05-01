package com.raja.myfyp.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.batterycharging.animation.chargingeffect.BroadCastReceivers.BootReceiver
import com.raja.myfyp.Activities.CallActivity
import com.raja.myfyp.R
import io.paperdb.Paper
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class CallService : Service() {
    private var batteryBoardcastReciver: BootReceiver? = null

    private val handler = Handler(Looper.getMainLooper())
    private val CHANNEL_ID = "SOS Application"
    private val FOREGROUND_SERVICE_ID = 101

    private val checkTimeRunnable = object : Runnable {
        override fun run() {
            val currentTime = Calendar.getInstance()
            val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
            val currentMinute = currentTime.get(Calendar.MINUTE)
            val currentTimeString = String.format("%02d:%02d:00", currentHour, currentMinute)
            val currentTimeMinutes = convertTimeStringToMinutes(currentTimeString)

            val fakeCallTime = Paper.book().read<String>("fakeCallTime", "")

            if (fakeCallTime != null) {
                if (fakeCallTime.isNotEmpty() && currentTimeString == fakeCallTime) {
                     val intent = Intent(this@CallService, CallActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    startActivity(intent)
                }
            }

            // Check again in 1 minute
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        val drawableIcon : Int = R.drawable.ic_launcher_foreground ?: R.drawable.ic_launcher_foreground
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("SOS App.")
            .setContentText("Fake Call Service Running...")
            .setSmallIcon(drawableIcon)
            .build()

        startForeground(FOREGROUND_SERVICE_ID, notification)
        batteryBoardcastReciver = BootReceiver()
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_TIME_TICK)
        registerReceiver(batteryBoardcastReciver, filter)

        val timer = Timer()
        val handler = Handler(Looper.getMainLooper())
        val doAsynchronousTask = object : TimerTask() {
            override fun run() {
                handler.post {
                }
            }
        }
        timer.schedule(doAsynchronousTask, 5000L, 5000L)

//        handler.post(checkTimeRunnable)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Battery Charging Animation is Started",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the callback to avoid memory leaks
//        handler.removeCallbacks(checkTimeRunnable)

        stopForeground(true)
    }

    private fun convertTimeStringToMinutes(timeString: String): Int {
        val timeParts = timeString.split(":")
        val hours = timeParts[0].toInt()
        val minutes = timeParts[1].toInt()

        return (hours * 60) + minutes
    }
}
