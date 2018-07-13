package top.xstar.plana.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import io.realm.Realm
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar.*
import top.xstar.plana.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter: RecycleBaseAdapter<BaseHolder, Memo>
    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addTask.setOnClickListener { addTask() }
        realm= Realm.getDefaultInstance()
        adapter = object : RecycleBaseAdapter<BaseHolder, Memo>() {
            init {
                layout = R.layout.tasklist_item_layout
                datas = ArrayList<Memo>()
            }

            override fun onBindView(holder: BaseHolder, position: Int, data: Memo) {
                holder.find<TextView>(R.id.task_description).text = data.content!!.lineSequence().first().trim()
                val nex_date = holder.find<TextView>(R.id.next_date)
                nex_date.text = Date(data.lastModifyTime).format("M月d日")
            }
        }
        taskList.adapter = adapter
        taskList.layoutManager=LinearLayoutManager(this)
        taskList.itemAnimator=DefaultItemAnimator()
        val decoration=DividerItemDecoration(this,LinearLayoutManager.VERTICAL)
        val divi=ColorDrawable(Color.GRAY)
        decoration.setDrawable(divi)
        taskList.addItemDecoration(decoration)
        toolbarTitle.setText(R.string.mainTitle)
    }

    fun addTask() {
        val intent = Intent(this, CreateTaskActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        realm.executeTransaction { it ->
            val list=it.where(Memo::class.java).sort("lastModifyTime",Sort.DESCENDING).findAll()
                    .toList()
            adapter.datas!!.clear()
            adapter.datas!!.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
