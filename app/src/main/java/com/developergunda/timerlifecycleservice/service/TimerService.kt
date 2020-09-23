package com.developergunda.timerlifecycleservice.service


import android.app.NotificationChannel
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_LOW
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.developergunda.timerlifecycleservice.model.TimerEvent
import com.developergunda.timerlifecycleservice.util.Constant
import com.developergunda.timerlifecycleservice.util.TimerUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : LifecycleService() {

    companion object {
        val timerEvent = MutableLiveData<TimerEvent>()
        val timerMillis = MutableLiveData<Long>()
    }

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder
    private var isStopedService = false

    override fun onCreate() {
        super.onCreate()
        initValues()
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

    private fun initValues() {
        timerEvent.postValue(TimerEvent.END)
        timerMillis.postValue(0L)
    }

    private fun startTimer() {

        val timerStarted = System.currentTimeMillis()
        CoroutineScope(Dispatchers.Main).launch {
            while (!isStopedService && timerEvent.value!! == TimerEvent.START) {
                val lapTime = System.currentTimeMillis() - timerStarted
                timerMillis.postValue(lapTime)
                delay(50L)
            }
        }

    }

    private fun startForeGroundService() {

        timerEvent.postValue(TimerEvent.START)
        startTimer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelNotification()

        }
        startForeground(Constant.NOTIFICATION_ID, notificationBuilder.build())

        timerMillis.observe(this, Observer {
            if (!isStopedService) {
                val builder = notificationBuilder.setContentText(
                    TimerUtil.getFormattedTime(it, false)
                )
                notificationManager.notify(Constant.NOTIFICATION_ID, builder.build())
            }
        })
    }

    private fun stopSrvice() {
        isStopedService = true
        initValues()
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


}