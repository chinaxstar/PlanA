package top.xstar.plana.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_create_task.*
import kotlinx.android.synthetic.main.include_toolbar.*
import top.xstar.plana.Memo
import top.xstar.plana.R
import top.xstar.plana.toast
import java.util.*


/**
 * @author: xstar
 * @since: 2018-04-26.
 */
class CreateTaskActivity : AppCompatActivity(), View.OnClickListener {

    var curMemo: Memo? = null

    companion object {
        const val MEMO_KEY = "parent.data.memo"
    }

    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        toolbarTitle.setText(R.string.editMemo)
        toolbarLeftBtn.setText(R.string.back)
        toolbarRightBtn.setText(R.string.save)
        curMemo = intent.getSerializableExtra(MEMO_KEY) as? Memo
        realm = Realm.getDefaultInstance()
        toolbarLeftBtn.setOnClickListener(this)
        toolbarRightBtn.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id) {
                R.id.toolbarLeftBtn -> finish()
                R.id.toolbarRightBtn -> saveMemo()
            }
        }
    }

    private fun saveMemo() {
        val text = content.text.toString()
        realm.executeTransaction{ r ->
            if (curMemo == null) curMemo = r.createObject(Memo::class.java, UUID.randomUUID().toString())
            curMemo?.let {

                it.lastModifyTime = System.currentTimeMillis()
                it.content = text
            }
            toast("保存成功！")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}