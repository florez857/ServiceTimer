package com.developergunda.timerlifecycleservice.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import com.developergunda.timerlifecycleservice.util.Constant
import timber.log.Timber

class TimerService : LifecycleService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {

                Constant.ACTION_START_SERVICE -> {
                    Timber.d("Start Service")
                }

                Constant.ACTION_STP_SERVICE -> {
                    Timber.d("Stop Service")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

}