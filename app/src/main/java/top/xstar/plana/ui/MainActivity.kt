package top.xstar.plana.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hwangjr.rxbus.RxBus
import io.realm.Realm
import top.xstar.plana.*

class MainActivity : AppCompatActivity() {

    var recyclerView: RecyclerView? = null
    var adapter: RecycleBaseAdapter<BaseHolder, TimeTask>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        RxBus.get().register(this)
        recyclerView = findViewById<RecyclerView>(R.id.taskList)
        adapter = object : RecycleBaseAdapter<BaseHolder, TimeTask>() {
            init {
                layout = R.layout.tasklist_item_layout
                datas = ArrayList<TimeTask>()
            }

            override fun onBindView(holder: BaseHolder, position: Int, data: TimeTask) {
                holder.find<TextView>(R.id.task_title).text = data.title
                holder.find<TextView>(R.id.task_description).text = data.description
                val nex_date = holder.find<TextView>(R.id.next_date)
                nex_date.text = AppUtil.getTaskNextDate(data).format("yyyy/MM/dd HH:mm:ss")
            }
        }
        recyclerView?.adapter = adapter
    }

    @OnClick(R.id.addTask)
    fun addTask() {
        val intent = Intent(this, CreateTaskActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        Realm.getDefaultInstance().copyFromRealm(adapter?.datas!!)
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        RxBus.get().unregister(this)
    }
}
