package com.developergunda.timerlifecycleservice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.developergunda.timerlifecycleservice.model.TimerEvent
import com.developergunda.timerlifecycleservice.service.TimerService
import com.developergunda.timerlifecycleservice.util.Constant
import com.developergunda.timerlifecycleservice.util.TimerUtil
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    var isTimerEnable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            toogleTimer()
        }
        startObserver()
    }

    private fun toogleTimer() {
        if (isTimerEnable) {
            sendCommandToServie(Constant.ACTION_STP_SERVICE)
        } else {
            sendCommandToServie(Constant.ACTION_START_SERVICE)
        }
    }


    private fun startObserver() {

        TimerService.timerEvent.observe(this, Observer {

            when (it) {

                is TimerEvent.START -> {
                    isTimerEnable = true
                    fab.setImageResource(R.drawable.twotone_stop_black_18)
                    Timber.d("Iniciamos el service evento start")
                }
                is TimerEvent.END -> {
                    isTimerEnable = false
                    fab.setImageResource(R.drawable.twotone_alarm_black_18)
                    Timber.d("Detenemos el service, evento end ")
                }
            }
        })


        TimerService.timerMillis.observe(this, Observer {
            tvTimer.text = TimerUtil.getFormattedTime(it, true)

        })
    }

    private fun sendCommandToServie(action: String) {
        startService(
            Intent(this, TimerService::class.java).apply {
                this.action = action
            }
        )

    }

}