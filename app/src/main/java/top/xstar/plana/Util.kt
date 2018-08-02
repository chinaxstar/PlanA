package top.xstar.plana

import android.app.Activity
import android.app.DialogFragment
import android.app.FragmentManager
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import top.xstar.plana.fragments.NoticeFragment


fun <T : TextView> RecyclerView.ViewHolder.find(id: Int): T {
    return itemView.findViewById<T>(id) as T
}

open class BaseHolder(item: View) : RecyclerView.ViewHolder(item)


enum class TimeUnit {
    YEAR, MONTH, DAY, HOUR, MINUTE, SECONDS, MILLI
}

fun showNoticeDialog(fragmentManager: FragmentManager, id: String?): DialogFragment {
    val dialogFragment = NoticeFragment()
    id?.let {
        val bundle = Bundle()
        bundle.putString(Const.ADD_NOTICE_ID, id)
        dialogFragment.arguments = bundle
    }
    dialogFragment.show(fragmentManager, "notice")
    return dialogFragment
}



