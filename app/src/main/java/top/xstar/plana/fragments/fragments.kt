package top.xstar.plana.fragments

import android.app.Activity
import android.content.Intent
import android.widget.TextView
import io.realm.Realm
import top.xstar.plana.*
import java.util.*

class NoticeFragment : BaseDialogFragment() {

    init {
        layoutId = R.layout.task_notice_dialog
        bindDialog = { view, builder ->
            val date = Date()
            val startDate = view.find<TextView>(R.id.start_date)
            startDate.text = date.format(Const.dateFomat)
            val endDate = view.find<TextView>(R.id.end_date)
            date.time += 24 * 60 * 60 * 1000
            endDate.text = date.format(Const.dateFomat)
            val intervalDate = view.find<TextView>(R.id.interval_date)
            intervalDate.text = "1"

            builder.setView(view)
            builder.setTitle("设置通知")
            builder.setNegativeButton("取消") { dialog, which ->
                dialog.cancel()
            }
            builder.setPositiveButton("确定") { dialog, which ->
                val realm = Realm.getDefaultInstance()
                val start = startDate.text.toString()
                val end = endDate.text.toString()
                val interval = intervalDate.text.toString().toInt()
                var id = arguments?.let { it.getString(Const.ADD_NOTICE_ID) }

                var notice: NoticeSet? = null
                realm.executeTransactionAsync({ r ->
                    if (id != null) notice = r.where(NoticeSet::class.java).equalTo("id", id).findFirst()
                    else {
                        id = UUID.randomUUID().toString()
                        notice = r.createObject(NoticeSet::class.java, id)
                    }
                    notice?.let { n ->
                        n.startTime = start.parse(Const.dateFomat).time
                        n.endTime = end.parse(Const.dateFomat).time
                        n.itervalTime = interval * Const.DAY_MILLI
                    }
                }, {
                    val intent = Intent()
                    intent.putExtra(Const.ADD_NOTICE_ID, id)
                    onActivityResult(Const.ADD_NOTICE_REQUEST, Activity.RESULT_OK, intent)
                    dismissAllowingStateLoss()
                    activity.toast("保存成功！")
                }) {
                    activity.toast("保存失败！")
                }

            }
        }
    }

}