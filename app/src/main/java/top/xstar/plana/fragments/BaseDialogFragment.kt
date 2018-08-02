package top.xstar.plana.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View

open class BaseDialogFragment : DialogFragment() {

    open var layoutId: Int = 0

    private lateinit var contentView: View

    open var bindDialog: ((View, AlertDialog.Builder) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        contentView = LayoutInflater.from(activity).inflate(layoutId, null, false)
        val builder = AlertDialog.Builder(activity)
        bindDialog?.let {
            it(contentView, builder)
        }
        return builder.create()
    }

}