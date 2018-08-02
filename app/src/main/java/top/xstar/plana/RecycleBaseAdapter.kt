package top.xstar.plana

import android.support.v7.widget.RecyclerView
import android.view.*

/**
 * @author: xstar
 * @since: 2018-02-28.
 */
abstract class RecycleBaseAdapter<T : BaseHolder, V> : RecyclerView.Adapter<T>() {
    var datas: MutableList<V>? = null
    var layout: Int = 0
    var haveHeader = false
    var haveFooter = false
    var headerLayout: Int = 0
    var footerLayout: Int = 0
    override fun onBindViewHolder(holder: T, position: Int) {
        if (haveHeader.and(position == 0)) {
            onBindHeader(holder, position)
            return
        }
        if (haveFooter.and(position == itemCount - 1)) {
            onBindFooter(holder, position)
            return
        }
        val data = datas!![position]
        onBindView(holder, position, data)
    }

    abstract fun onBindView(holder: T, position: Int, data: V)
    open fun onBindHeader(holder: T, position: Int) {}
    open fun onBindFooter(holder: T, position: Int) {}
    var inflate: LayoutInflater? = null
    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T {
        if (inflate == null) inflate = LayoutInflater.from(parent.context)
        val temp = inflate!!.inflate(viewType, parent, false)
        return BaseHolder(temp) as T
    }

    override fun getItemCount(): Int {
        var count = datas?.size ?: 0
        if (haveHeader) count++
        if (haveFooter) count++
        return count
    }

    override fun getItemViewType(position: Int): Int {
        val layoutTemp: Int
        if (haveHeader.and(position == 0)) {
            layoutTemp = headerLayout
        } else if (haveFooter.and(position == itemCount - 1)) {
            layoutTemp = footerLayout
        } else {
            layoutTemp = layout
        }
        return layoutTemp
    }

    var gesture: GestureDetector? = null

    var itemClick: ((View, Int, MotionEvent) -> Unit)? = null
    var itemFling: ((View, Int, MotionEvent, MotionEvent, Float, Float) -> Unit)? = null
    var itemLongPress: ((View, Int, MotionEvent) -> Unit)? = null
    fun bindItemListener(rv: RecyclerView) {
        if (gesture == null) {
            gesture = GestureDetector(rv.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent?): Boolean {
                    e?.let {
                        val view = rv.findChildViewUnder(e.x, e.y)
                        view?.let { v ->
                            val pos = rv.getChildLayoutPosition(v)
                            itemClick?.let { click ->
                                click(v, pos, e)
                                return true
                            }
                        }
                    }
                    return false
                }

                override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                    val item = e1?.let {
                        return@let rv.findChildViewUnder(it.x, it.y)
                    }
                    val item2 = e2?.let {
                        return@let rv.findChildViewUnder(it.x, it.y)
                    }
                    if (item != null && item == item2) {
                        itemFling?.let {
                            it(item, rv.getChildLayoutPosition(item), e1, e2, velocityX, velocityY)
                        }
                        return true
                    }
                    return false
                }

                override fun onLongPress(e: MotionEvent?) {
                    e?.let {
                        val view = rv.findChildViewUnder(e.x, e.y)
                        view?.let { v ->
                            val pos = rv.getChildLayoutPosition(v)
                            itemLongPress?.let { click ->
                                click(v, pos, e)
                            }
                        }
                    }
                }
            })


        }
        rv.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
                return gesture!!.onTouchEvent(e)
            }
        })
    }

}