package com.developergunda.timerlifecycleservice.service

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.developergunda.timerlifecycleservice.model.TimerEvent
import com.developergunda.timerlifecycleservice.util.Constant
import timber.log.Timber

class TimerService : LifecycleService() {

    companion object {
        val timerEvent = MutableLiveData<TimerEvent>()
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        intent?.let {
            when (it.action) {

                Constant.ACTION_START_SERVICE -> {
                    Timber.d("Start Service")
                    timerEvent.postValue(TimerEvent.START)
                }

                Constant.ACTION_STP_SERVICE -> {
                    Timber.d("Stop Service")
                    timerEvent.postValue(TimerEvent.END)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

}