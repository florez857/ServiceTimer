package com.developergunda.timerlifecycleservice.service

import android.app.NotificationChannel
import android.app.PendingIntent


import android.content.Intent
import android.os.Build

import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_LOW
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.developergunda.timerlifecycleservice.MainActivity
import com.developergunda.timerlifecycleservice.R
import com.developergunda.timerlifecycleservice.model.TimerEvent
import com.developergunda.timerlifecycleservice.util.Constant

class TimerService : LifecycleService() {

    companion object {
        val timerEvent = MutableLiveData<TimerEvent>()
        val timerMillis = MutableLiveData<Long>()
    }

    private lateinit var notificationManager: NotificationManagerCompat
    override fun onCreate() {
        super.onCreate()
        notificationManager = NotificationManagerCompat.from(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {

                Constant.ACTION_START_SERVICE -> {
                    startForeGroundService()
                }

                Constant.ACTION_STP_SERVICE -> {
                    stopSrvice()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }


    private fun startForeGroundService() {
        timerEvent.postValue(TimerEvent.START)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelNotification()

        }
        startForeground(Constant.NOTIFICATION_ID, getNotificationBuilde().build())
    }

    private fun stopSrvice() {
        timerEvent.postValue(TimerEvent.END)
        notificationManager.cancel(Constant.NOTIFICATION_ID)
        stopForeground(true)
        stopSelf()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannelNotification() {
        val channel = NotificationChannel(
            Constant.NOTIFICATION_CHANNEL_ID,
            Constant.NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun getNotificationBuilde() =
        NotificationCompat.Builder(this, Constant.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)//mantiene la notificacion arriba de todas las notificaciones
            .setSmallIcon(R.drawable.twotone_alarm_black_24)
            .setContentTitle("Servicio de timer ejecutando")
            .setContentText("00:00:00:00")
            .setContentIntent(mainActivityPendingIntent())


    private fun mainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        143,
        Intent(this, MainActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}