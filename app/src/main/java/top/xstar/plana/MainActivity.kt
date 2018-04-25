package top.xstar.plana

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.taskList)
        recyclerView?.adapter = object : RecycleBaseAdapter<BaseHolder, TimeTask>() {
            init {
                layout = R.layout.tasklist_item_layout
            }

            override fun onBindView(holder: BaseHolder, position: Int, data: TimeTask) {
                holder.find<TextView>(R.id.task_title).text = data.title
                holder.find<TextView>(R.id.task_description).text = data.description
                val nex_date = holder.find<TextView>(R.id.next_date)
                nex_date.text = AppUtil.getTaskNextDate(data).format("yyyy/MM/dd HH:mm:ss")
            }
        }

    }
}
