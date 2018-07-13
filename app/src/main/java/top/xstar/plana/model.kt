package top.xstar.plana

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*

/**
 * @author: xstar
 * @since: 2018-04-24.
 */

/**
 *
 * 备忘录
 * content 备忘录内容 html
 * 修改时间
 * 提醒设置ID
 */
open class Memo : RealmObject(), Serializable {
    @PrimaryKey
    var id: String? = null
    var content: String? = null
    var lastModifyTime: Long = 0
    var noticeId: String? = null
}

/**
 * 提醒设置
 * 一些备忘录内容需要提醒
 * 开始日期
 * 结束日期
 * 间隔®
 */
open class NoticeSet : RealmObject(), Serializable {
    @PrimaryKey
    var id: String? = null
    var startTime = 0L
    var endTime = 0L
    var itervalTime = 0L
}


