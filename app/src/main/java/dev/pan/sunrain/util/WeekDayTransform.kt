package dev.pan.sunrain.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getWeekdayName(dateString: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = sdf.parse(dateString)
    val calendar = Calendar.getInstance()
    calendar.time = date!!
    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    return when (dayOfWeek) {
        Calendar.SUNDAY -> "Sunday"
        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        else -> "Invalid day"
    }
}