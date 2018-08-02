package top.xstar.plana

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Toast
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
fun Context.toast(textId: Int) = toast(getString(textId))
fun Activity.toast(text: String) = applicationContext.toast(text)
fun Activity.toast(textId: Int) = applicationContext.toast(textId)

fun <T : View> View.find(id: Int): T {
    return findViewById<T>(id)
}

fun <T : View> Activity.find(id: Int): T {
    return findViewById<T>(id)
}

fun <T : View> Fragment.find(id: Int): T {
    return view.findViewById<T>(id)
}


fun Date.format(pattern: String): String {
    return SimpleDateFormat(pattern).format(this)
}

fun String.parse(pattern: String = "yyyy-MM-dd"): Date {
    return SimpleDateFormat(pattern).parse(this)
}

inline fun <T : Activity> routeTo(context: Context, clazz: Class<T>, init: (Intent) -> Intent = { it }) {
    context.startActivity(init(Intent(context, clazz)))
}

inline fun <T : Activity> Activity.routeTo(clazz: Class<T>, init: (Intent) -> Intent = { it }) {
    startActivity(init(Intent(applicationContext, clazz)))
}

inline fun <T : Activity> Activity.routeForResult(clazz: Class<T>, code: Int, init: (Intent) -> Intent = { it }) {
    startActivityForResult(init(Intent(applicationContext, clazz)), code)
}

fun Intent.withFlag(flag: Int): Intent {
    flags = flag
    return this
}

fun Intent.withAction(action: String): Intent {
    this.action = action
    return this
}

fun Intent.withBundle(extras: Bundle): Intent {
    this.putExtras(extras)
    return this
}

fun <T> Intent.withExtra(key: String, value: T): Intent {
    when (value) {
        is Int -> this.putExtra(key, value)
        is String -> this.putExtra(key, value)
        is Float -> this.putExtra(key, value)
        is Double -> this.putExtra(key, value)
        is Short -> this.putExtra(key, value)
        is Serializable -> this.putExtra(key, value)
        is Parcelable -> this.putExtra(key, value)
    }
    return this
}

inline fun <T> Any.getTClass(): Class<T>? {
    return (javaClass.genericSuperclass as? ParameterizedType)?.let {
        it.actualTypeArguments[0] as? Class<T>
    }
}


fun <T> Intent.withArray(key: String, array: Array<T>): Intent {
    val type = array.getTClass<T>()
    type?.let {
        when (type.simpleName) {
            "Int" -> {
                val list = ArrayList<Int>()
                list.addAll(array as Array<out Int>)
                putIntegerArrayListExtra(key, list)
            }
            "String" -> {
                val list = ArrayList<String>()
                list.addAll(array as Array<out String>)
                putStringArrayListExtra(key, list)
            }
            else -> {
            }
        }

    }
    return this
}

fun logger(lv: String, text: String) {
    val stacks = Thread.currentThread().stackTrace
    if (stacks.isNotEmpty()) {
        val tag = stacks[0].className
        val msg = stacks[0].methodName + text
        when (lv) {
            Const.VERBOSE -> Log.v(tag, msg)
            Const.INFO -> Log.i(tag, msg)
            Const.WARN -> Log.w(tag, msg)
            Const.ERROR -> Log.e(tag, msg)
            else -> Log.w(tag, lv + msg)
        }

    }
}

fun Activity.logI(text: String) {
    logger(Const.INFO, text)
}

fun Activity.logE(text: String) {
    logger(Const.ERROR, text)
}

fun Activity.showDialog(init: (AlertDialog.Builder) -> Unit = {}): AlertDialog {
    val build = AlertDialog.Builder(this)
    init(build)
    val dialog = build.create()
    dialog.show()
    return dialog
}

fun Fragment.showDialog(init: (AlertDialog.Builder) -> Unit = {}): AlertDialog {
    return activity.showDialog(init)
}