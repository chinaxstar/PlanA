package top.xstar.plana.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_create_task.*
import kotlinx.android.synthetic.main.include_toolbar.*
import top.xstar.plana.*
import java.util.*


/**
 * @author: xstar
 * @since: 2018-04-26.
 */
class CreateTaskActivity : AppCompatActivity(), View.OnClickListener {

    var curMemo: Memo? = null

    lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        toolbarTitle.setText(R.string.editMemo)
        toolbarLeftBtn.setText(R.string.back)
        toolbarRightBtn.setText(R.string.save)
        val id = intent.getStringExtra(Const.MEMO_KEY)
        realm = Realm.getDefaultInstance()
        curMemo = id?.let {
            realm.where(Memo::class.java).equalTo("id", it).findFirst()
        }
        toolbarLeftBtn.setOnClickListener(this)
        toolbarRightBtn.setOnClickListener(this)
        notice.setOnClickListener(this)
        curMemo?.let {
            content.setText(it.content)
        }

    }

    override fun onClick(p0: View?) {
        p0?.let {
            when (it.id) {
                R.id.toolbarLeftBtn -> finish()
                R.id.toolbarRightBtn -> saveMemo()
                R.id.notice -> {
                    if (curMemo != null) {
                        showNoticeDialog(fragmentManager, curMemo?.notice?.id)
                    } else {
                        toast("请先保存备忘录！")
                    }
                }
                else -> {
                    toast("未知操作！")
                }
            }
        }
    }

    private fun saveMemo() {
        val text = content.text.toString()
        realm.executeTransaction { r ->
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == Const.ADD_NOTICE_REQUEST) {
            data?.let {
                val id = it.getStringExtra(Const.ADD_NOTICE_ID)
                realm.executeTransaction { r ->
                    val noticeSet = r.where(NoticeSet::class.java).equalTo("id", id).findFirst()
                    if (noticeSet != null && curMemo != null) {

                    }
                }
            }
        }
    }
}