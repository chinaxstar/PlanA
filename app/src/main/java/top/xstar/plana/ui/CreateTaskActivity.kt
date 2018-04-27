package top.xstar.plana.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TimePicker
import top.xstar.plana.R
import top.xstar.plana.R.id.taskTypeSpinner
import top.xstar.plana.R.id.timePicker

/**
 * @author: xstar
 * @since: 2018-04-26.
 */
class CreateTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        val timePicker = findViewById<TimePicker>(timePicker)
        timePicker.setIs24HourView(true)
        timePicker.currentHour = 8
        timePicker.currentMinute = 30

    }
}