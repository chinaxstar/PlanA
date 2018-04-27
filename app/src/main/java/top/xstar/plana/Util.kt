package top.xstar.plana

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


inline fun <T : TextView> RecyclerView.ViewHolder.find(id: Int): T {
    return itemView.findViewById<T>(id) as T
}

inline fun Date.format(pattern: String): String {
    val f = AppUtil.singleDateFormat(pattern)
    return f.format(this)
}

/**
 * @author: xstar
 * @since: 2018-02-28.
 */
object AppUtil {
    fun singleDateFormat(pattern: String): SimpleDateFormat {
        return SimpleDateFormat(pattern)
    }

    fun <T : View> find(view: View, id: Int): T {
        return view.findViewById<T>(id) as T
    }

    fun <T : View> find(act: Activity, id: Int): T {
        return act.findViewById<T>(id) as T
    }

    /**
     * 短长toast
     */
    fun sShow(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    /**
     * 长toast
     */
    fun lShow(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val res = context.resources
        val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    fun setStatusBarColor(act: Activity, color: Int, whiteStatusIcon: Boolean) {
        val window = act.window
        if (Build.VERSION.SDK_INT >= 23) {
            val decor = window.decorView
            var ui = decor.systemUiVisibility
            if (whiteStatusIcon) {
                ui = ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                ui = ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            decor.systemUiVisibility = ui
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            //设置状态栏颜色
            window.statusBarColor = color
        } else if (Build.VERSION.SDK_INT >= 19) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    fun uri2RealPath(context: Context, uri: Uri): String? {
        var scheme: String? = uri.scheme ?: return uri.path
        var data: String? = null
        when (scheme) {
            ContentResolver.SCHEME_FILE -> uri.path
            ContentResolver.SCHEME_CONTENT -> {
                val cursor = context.contentResolver.query(uri, Array(1, { MediaStore.Images.ImageColumns.DATA }), null, null, null)
                cursor.moveToFirst()
                val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                if (index != -1)
                    data = cursor?.getString(index)
                cursor?.close()
            }
        }
        return data
    }

    fun getExsitFile(path: String): File? {
        val file = File(path)
        if (file.exists()) return file
        return null
    }

    fun getTaskNextDate(task: TimeTask): Date {
        when (task.taskType) {
            TaskType.SINGLE -> {
                return task.startDate
            }
            TaskType.EVERYDAY -> {
                val calendar = Calendar.getInstance()
                calendar.time = Date()
                val calendar2 = Calendar.getInstance()
                calendar2.time = task.startDate
                calendar2.set(Calendar.SECOND, 0)
                val h = calendar2.get(Calendar.HOUR)
                val m = calendar2.get(Calendar.MINUTE)
                calendar.set(Calendar.HOUR, h)
                calendar.set(Calendar.MINUTE, m)
                return calendar.time
            }
            else ->{
                return task.startDate
            }
        }
    }
}

object PrefsUtil {
    var prefs: SharedPreferences? = null
    var gson: Gson? = null
    open fun init(context: Context) {
        prefs = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
        gson = Gson()
    }

    open fun <T> getObj(key: String, tClass: Class<T>): T {
        val json = prefs!!.getString(key, "{}")
        return gson!!.fromJson<T>(json, tClass)
    }

    open fun <T> getObj(tClass: Class<T>): T {
        return getObj(tClass.name, tClass)
    }

    open fun saveObj(key: String, obj: Any): Boolean {
        val json = gson!!.toJson(obj)
        return prefs!!.edit().putString(key, json).commit()
    }

    open fun saveObj(obj: Any): Boolean {
        return saveObj(obj.javaClass.name, obj)
    }
}

open class BaseHolder(item: View) : RecyclerView.ViewHolder(item)


