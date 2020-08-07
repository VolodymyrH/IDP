package com.vholodynskyi.speedometer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        accelerate.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                viewModel.clickStart()
                v.performClick()
            }

            if (event.action == MotionEvent.ACTION_UP) {
                viewModel.clickEnd()
            }
            true
        }

        break_btn.setOnClickListener {
            speedometer.breakClicked()
        }

        viewModel.timeStream.observe(this, Observer {
            speedometer.startAcceleration()
        })
    }
}