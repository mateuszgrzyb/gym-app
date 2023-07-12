package mateuszgrzyb.gym_app.data

import android.content.ContentValues
import android.provider.CalendarContract
import com.philjay.RRule
import java.util.Calendar
import java.util.TimeZone

data class CalendarEventData(
    val title: String,
    val description: String?,
    val workoutHours: Int,
    val weekStart: Int,
    val rRule: RRule,
) {
    private fun dtStart(): Long =
        Calendar.getInstance().apply {
            add(Calendar.WEEK_OF_YEAR, weekStart)
        }.timeInMillis

    private fun dtEnd(): Long =
        Calendar.getInstance().apply {
            add(Calendar.WEEK_OF_YEAR, weekStart)
            add(Calendar.HOUR_OF_DAY, workoutHours)
        }.timeInMillis

    private fun timezone(): String =
        TimeZone.getDefault().id

    private fun rRule(): String =
        rRule.toRFC5545String().split(":")[1]

    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put(CalendarContract.Events.DTSTART, dtStart())
            put(CalendarContract.Events.DTEND, dtEnd())
            put(CalendarContract.Events.EVENT_TIMEZONE, timezone())
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.DESCRIPTION, description)
            put(CalendarContract.Events.CALENDAR_ID, 1)
            put(CalendarContract.Events.RRULE, rRule())
            put(CalendarContract.Events.ALL_DAY, true)
        }
    }
}