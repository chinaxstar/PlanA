package top.xstar.plana.ui

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_create_task.*
import top.xstar.plana.R
import top.xstar.plana.TaskType

/**
 * @author: xstar
 * @since: 2018-04-26.
 */
class CreateTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        if (Build.VERSION.SDK_INT >= 23) {
            timePicker.hour = 8
            timePicker.minute = 30
        } else {
            timePicker.currentHour = 8
            timePicker.currentMinute = 30
        }
        timePicker.setIs24HourView(true)
        taskTypeSpinner.adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Array(6) {
            when (it) {
                TaskType.WORK -> "工作日"
                TaskType.EVERYDAY -> "每天"
                TaskType.SINGLE -> "单次"
                TaskType.HOLIDAY -> "节假日"
                TaskType.MEMORIALDAY -> "纪念日"
                TaskType.NUMBERS -> "间隔天数"
                else -> "工作日"
            }
        }) {}
        accuracySpinner.adapter = object : ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Array(4) {
            when (it) {
                0 -> "日"
                1 -> "周"
                2 -> "月"
                3 -> "年"
                else -> "日"
            }
        }) {}

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }
}