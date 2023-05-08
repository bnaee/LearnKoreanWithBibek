package com.genesiswtech.lkwb.utils

import android.content.Context
import com.genesiswtech.lkwb.R
import java.util.*

object TimeAgo {

    private const val SECOND = 1
    private const val MINUTE = 60 * SECOND
    private const val HOUR = 60 * MINUTE
    private const val DAY = 24 * HOUR
    private const val WEEK = 7 * DAY
    private const val MONTH = 30 * DAY
    private const val YEAR = 12 * MONTH

    private fun currentDate(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }

    fun getTimeAgo(context: Context, date: Date): String {
        val time: Long = date.time
        val now: Long = currentDate()
        if (time > now || time <= 0) {
            return context.getString(R.string.in_the_future)
        }
        val zone: Long = 20700000
        val diff = (now - time - zone) / 1000
        return when {
            diff < 2 * SECOND -> context.getString(R.string.now)
            diff < 1 * MINUTE -> String.format(
                context.getString(
                    R.string.seconds_ago
                ), diff / SECOND
            )
            diff < 2 * MINUTE -> context.getString(R.string.minute_ago)
            diff < 60 * MINUTE -> String.format(
                context.getString(
                    R.string.minutes_ago
                ), diff / MINUTE
            )
            diff < 2 * HOUR -> context.getString(R.string.hour_ago)
            diff < 24 * HOUR -> String.format(
                context.getString(
                    R.string.hours_ago
                ), diff / HOUR
            )
            diff < 2 * DAY -> String.format(
                context.getString(
                    R.string.day_ago
                ), diff / DAY
            )
            diff < 7 * DAY -> String.format(
                context.getString(
                    R.string.days_ago
                ), diff / DAY
            )
            diff < 8 * DAY -> context.getString(R.string.week_ago)
            diff < 1 * MONTH -> String.format(
                context.getString(
                    R.string.weeks_ago
                ), diff / WEEK
            )
            diff < 2 * MONTH -> context.getString(R.string.month_ago)
            diff < 12 * MONTH -> String.format(
                context.getString(
                    R.string.months_ago
                ), diff / MONTH
            )
            diff < 2 * YEAR -> context.getString(R.string.year_ago)
            else -> String.format(
                context.getString(
                    R.string.years_ago
                ), diff / YEAR
            )
        }
    }
}