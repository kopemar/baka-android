package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Deprecated("Replaced with full date", ReplaceWith("fullDate()"))
fun String.fullDateShortDayOfWeek(): String = this.fullDate().toString()

fun String.fullDate(): String? =
    ZonedDateTime.parse(this).format(formatWithZone(DateTimeFormats.DAY_MONTH_DAY_YEAR))

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

fun String.dayMonth(): String =
    ZonedDateTime.parse(this).format(formatWithZone(DateTimeFormats.FULL_MONTH_DAY))

fun String.dayOfWeek(): String =
    ZonedDateTime.parse(this).toOffsetDateTime().format(formatWithZone(DateTimeFormats.WEEK_DAY))

fun formatWithZone(pattern: String) =
    formatter(pattern).withZone(ZoneId.systemDefault())

fun formatWithZone(format: DateTimeFormats) =
    formatWithZone(if (isCzech()) format.czech else format.english)

fun formatter(format: DateTimeFormats) =
    formatter(if (isCzech()) format.czech else format.english)

fun formatter(pattern: String) = DateTimeFormatter.ofPattern(pattern)

fun isCzech() = Locale.getDefault().country == "CZ"

enum class DateTimeFormats(val english: String, val czech: String = english) {
    WEEK_DAY("EEEE"),
    SHORT_DAY("E "),
    DAY_MONTH_DAY("EE, MMMM d", "EE d. MMMM"),
    DAY_MONTH_DAY_YEAR("EE, MMMM d YYYY", "EE d. MMMM YYYY"),
    FULL_MONTH_DAY("MMMM d", "d. MMMM"),
    HOURS_MINUTES("hh:mm a", "HH:mm"),
}