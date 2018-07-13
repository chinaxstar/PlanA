package top.xstar.plana

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView


fun <T : TextView> RecyclerView.ViewHolder.find(id: Int): T {
    return itemView.findViewById<T>(id) as T
}

open class BaseHolder(item: View) : RecyclerView.ViewHolder(item)




