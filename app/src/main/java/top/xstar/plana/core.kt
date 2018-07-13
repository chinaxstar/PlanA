package top.xstar.plana

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.view.View
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

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