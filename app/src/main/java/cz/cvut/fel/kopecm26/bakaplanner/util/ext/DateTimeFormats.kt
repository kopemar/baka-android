package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun String.fullDateShortDayOfWeek(): String? = LocalDate.parse(substring(0, 10)).format(DateTimeFormatter.ofPattern(DateTimeFormats.FULL_DATE_SHORT_DAY))
fun String.hoursAndMinutes(): String = ZonedDateTime.parse(this).format(DateTimeFormatter.ofPattern(DateTimeFormats.HOURS_MINUTES)).toUpperCase(
    Locale.getDefault())

object DateTimeFormats {
    const val FULL_DATE_SHORT_DAY = "E, MMMM d, yyyy"
    const val HOURS_MINUTES = "HH:MM a"
}