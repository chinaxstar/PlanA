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
import android.support.v7.widget.helper.ItemTouchHelper
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
        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

            }
        })
        touchHelper.attachToRecyclerView(taskList)
        realm = Realm.getDefaultInstance()
        adapter = object : RecycleBaseAdapter<BaseHolder, Memo>() {
            init {
                layout = R.layout.tasklist_item_layout
                datas = ArrayList<Memo>()
                itemClick = { v, i, e ->
                    routeTo(CreateTaskActivity::class.java) {
                        it.withFlag(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                                .withExtra(Const.MEMO_KEY, datas!!.get(i).id)
                    }
                }
                itemFling = { v, i, e1, e2, vx, vy ->
                    val sx = e1.x - e2.x
                    val sy = e1.y - e2.y
                    logE(String.format("sx:%s sy:%s", sx, sy))
                }

                itemLongPress = { v, i, e ->
                    touchHelper.startDrag(taskList.getChildViewHolder(v))
                }
            }


            override fun onBindView(holder: BaseHolder, position: Int, data: Memo) {
                holder.find<TextView>(R.id.task_description).text = data.content!!.lineSequence().first().trim()
                val nex_date = holder.find<TextView>(R.id.next_date)
                nex_date.text = Date(data.lastModifyTime).format("M月d日")
            }
        }
        taskList.adapter = adapter
        adapter.bindItemListener(taskList)
        taskList.layoutManager = LinearLayoutManager(this)
        taskList.itemAnimator = DefaultItemAnimator() as RecyclerView.ItemAnimator
        val decoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        val divi = ColorDrawable(Color.GRAY)
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
            val list = it.where(Memo::class.java).sort("lastModifyTime", Sort.DESCENDING).findAll()
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


