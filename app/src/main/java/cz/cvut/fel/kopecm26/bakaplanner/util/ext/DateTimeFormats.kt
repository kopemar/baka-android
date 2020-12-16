package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun String.fullDateShortDayOfWeek(): String =
    StringBuilder(
        ZonedDateTime.parse(this).format(DateTimeFormatter.ofPattern(DateTimeFormats.SHORT_DAY))
    )
        .append(fullDate()).toString()

fun String.fullDate(): String? =
    ZonedDateTime.parse(this).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))

fun String.hoursAndMinutes(): String =
    ZonedDateTime.parse(this).format(DateTimeFormatter.ofPattern(DateTimeFormats.HOURS_MINUTES))
        .toUpperCase(
            Locale.getDefault()
        )

fun String.dayOfWeek(): String =
    ZonedDateTime.parse(this).format(DateTimeFormatter.ofPattern(DateTimeFormats.WEEK_DAY))

object DateTimeFormats {
    const val WEEK_DAY = "EEEE"
    const val SHORT_DAY = "E "
    const val FULL_DATE_SHORT_DAY = "E, MMMM d, yyyy"
    const val HOURS_MINUTES = "hh:mm a"
}