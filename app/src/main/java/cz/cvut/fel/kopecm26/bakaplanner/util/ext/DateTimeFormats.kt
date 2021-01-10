package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun String.fullDateShortDayOfWeek(): String =
    StringBuilder(
        ZonedDateTime.parse(this).format(formatWithZone(DateTimeFormats.SHORT_DAY))
    ).append(fullDate()).toString()

fun String.fullDate(): String? =
    ZonedDateTime.parse(this).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))

fun String.hoursAndMinutes(): String =
    ZonedDateTime.parse(this).format(formatWithZone(DateTimeFormats.HOURS_MINUTES))
        .toUpperCase(
            Locale.getDefault()
        )

fun LocalTime.hoursAndMinutes(): String =
    format(formatWithZone(DateTimeFormats.HOURS_MINUTES))
        .toUpperCase(
            Locale.getDefault()
        )

fun String.dayMonth(): String = ZonedDateTime.parse(this).format(formatWithZone(DateTimeFormats.FULL_MONTH_DAY))

fun String.dayOfWeek(): String =
    ZonedDateTime.parse(this).toOffsetDateTime().format(formatWithZone(DateTimeFormats.WEEK_DAY))

fun formatWithZone(pattern: String) = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())

fun formatter(pattern: String) = DateTimeFormatter.ofPattern(pattern)

object DateTimeFormats {
    const val WEEK_DAY = "EEEE"
    const val SHORT_DAY = "E "
    const val FULL_DATE_SHORT_DAY = "EE, MMMM d"
    const val FULL_MONTH_DAY = "MMMM d"
    const val HOURS_MINUTES = "hh:mm a"
}