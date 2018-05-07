package top.xstar.plana

import io.realm.RealmObject
import java.util.*

/**
 * @author: xstar
 * @since: 2018-04-24.
 */

/**
 * 定时任务类
 * 任务标题
 * 任务描述
 * 任务创建时间
 * 任务结束时间
 * 任务类型
 * 开始时间 闹钟使用HH:MM
 * 精度 （分、时、日期）
 */
open class TimeTask : RealmObject() {
    var id: String = UUID.randomUUID().toString()
    var title: String? = null
    var description: String? = null
    var createDate: Date? = null
    var endDate: Date? = null
    var taskType: Int = TaskType.EVERYDAY
    var startDate: Date? = null
    var accuracy: Int = TaskAccuracy.MINUTE
}

object TaskType {
    /**
     * 一次
     */
    val SINGLE = 0
    /**
     * 工作日
     */
    val WORK = 1
    /**
     * 节假日
     */
    val HOLIDAY = 2
    /**
     * 每天
     */
    val EVERYDAY = 3
    /**
     * 每经过多少天
     */
    val NUMBERS = 4
    /**
     * 纪念日
     */
    val MEMORIALDAY = 5

}

object TaskAccuracy {
    val MINUTE = 0
    val HOUR = 0
    val DAY = 0
}
