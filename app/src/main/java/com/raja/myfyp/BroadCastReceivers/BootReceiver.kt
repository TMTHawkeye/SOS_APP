package com.batterycharging.animation.chargingeffect.BroadCastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
 import android.util.Log
import com.raja.myfyp.Activities.CallActivity
import io.paperdb.Paper
import java.util.Calendar

class BootReceiver() : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        try {
            val intentString = intent.action

            when (intentString) {
                Intent.ACTION_TIME_TICK->{
                    val currentTime = Calendar.getInstance()
                    val currentHour = currentTime.get(Calendar.HOUR_OF_DAY)
                    val currentMinute = currentTime.get(Calendar.MINUTE)
                    val currentTimeString = String.format("%02d:%02d:00", currentHour, currentMinute)

                    val fakeCallTime = Paper.book().read<String>("fakeCallTime", "")

                    if (fakeCallTime != null) {
                        if (fakeCallTime.isNotEmpty() && currentTimeString == fakeCallTime) {
                            val intent = Intent(context, CallActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        }
                    }
                }


            }
        } catch (e: Exception) {
            Log.e("Receiver", "Error in onReceive: ${e.message}")
        }
    }


}
