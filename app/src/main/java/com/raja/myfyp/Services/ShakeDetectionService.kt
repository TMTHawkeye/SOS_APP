package com.raja.myfyp.Services

// ShakeDetectionService.kt
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.raja.myfyp.R
import java.util.Timer
import java.util.TimerTask

class ShakeDetectionService : Service(), SensorEventListener {
    private val FOREGROUND_SERVICE_ID = 101
    private val CHANNEL_ID = "SOS Application"

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var sosServiceStarted = false

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
            .setContentText("Shake to send SOS Service Running...")
            .setSmallIcon(drawableIcon)
            .build()

        startForeground(FOREGROUND_SERVICE_ID, notification)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.also { accel ->
            sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    private fun startSosSendingService() {
        if (!sosServiceStarted) {
            val intent = Intent(applicationContext, SOSService::class.java)
            startService(intent)
            sosServiceStarted = true
             Toast.makeText(this, "Shake detected! SOS initiated.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            // Shake detection logic
            val acceleration = Math.sqrt((x * x + y * y + z * z).toDouble())
            if (acceleration > SHAKE_THRESHOLD) {
                startSosSendingService()
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        stopForeground(true)

    }

    companion object {
        // Shake threshold (adjust as needed)
        private const val SHAKE_THRESHOLD = 90
    }
}
