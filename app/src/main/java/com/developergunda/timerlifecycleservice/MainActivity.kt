package com.developergunda.timerlifecycleservice

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.developergunda.timerlifecycleservice.service.TimerService
import com.developergunda.timerlifecycleservice.util.Constant
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            sendCommandToServie(Constant.ACTION_START_SERVICE)
        }
    }

    private fun sendCommandToServie(action: String) {
        startService(
            Intent(this, TimerService::class.java).apply {
                this.action = action
            }
        )

    }

}