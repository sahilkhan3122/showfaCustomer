package utils

import android.text.format.DateFormat
import java.util.Calendar

object DateAndDayFormat {

    fun getFormatDateForDay(time: String): String? {
        val value = "-"
        if (time == "0" || time == "") {
            return value
        }
        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = time.toLong() * 1000L
        val now = Calendar.getInstance()
        val timeFormatString = "h:mm aa"
        val dateTimeFormatString = "EEEE, MMMM d, h:mm aa"
        (60 * 60 * 60).toLong()
        return if (now[Calendar.DATE] == smsTime[Calendar.DATE]) {
            "Today, " + DateFormat.format(timeFormatString, smsTime)
        } else if (now[Calendar.DATE] - smsTime[Calendar.DATE] == 1) {
            "Yesterday, " + DateFormat.format(timeFormatString, smsTime)
        } else if (now[Calendar.YEAR] == smsTime[Calendar.YEAR]) {
            DateFormat.format(dateTimeFormatString, smsTime).toString()
        } else {
            DateFormat.format("MMMM dd yyyy, h:mm aa", smsTime).toString()
        }
    }

}