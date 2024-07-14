package com.chat.compose.app.util

import java.util.*
import kotlin.math.abs

/**
 * @author liuzhongao
 * @since 2024/6/19 00:53
 */
fun formatTime(time: Long): String {
    val (todayTime, targetTime) = formatTimes(time)

    if (abs(todayTime.year - targetTime.year) > 0) {
        return "${targetTime.year}/${targetTime.month}/${targetTime.day}"
    } else if (abs(todayTime.month - targetTime.month) > 0 || abs(todayTime.day - targetTime.day) > 0) {
        return "${targetTime.month}/${targetTime.day}"
    }

    return if (abs(todayTime.hour - targetTime.hour) > 0) {
        String.format("%02d:%02d", targetTime.hour, targetTime.minute)
    } else if (abs(todayTime.minute - targetTime.minute) > 10) {
        String.format("%02d:%02d", targetTime.hour, targetTime.minute)
    } else if (abs(todayTime.minute - targetTime.minute) > 0) {
        "${todayTime.minute - targetTime.minute}分钟前"
    } else {
        "刚刚"
    }
}

private fun formatTimes(time: Long): Pair<Time, Time> {
    val calendar = Calendar.getInstance()
    val todayTime = Time(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH),
        hour = calendar.get(Calendar.HOUR_OF_DAY),
        minute = calendar.get(Calendar.MINUTE),
        second = calendar.get(Calendar.SECOND)
    )

    calendar.timeInMillis = time
    val targetTime = Time(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH),
        hour = calendar.get(Calendar.HOUR_OF_DAY),
        minute = calendar.get(Calendar.MINUTE),
        second = calendar.get(Calendar.SECOND)
    )
    return todayTime to targetTime
}

private data class Time(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int
)