package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@Deprecated("Replaced with full date", ReplaceWith("fullDate()"))
fun String.fullDateShortDayOfWeek(): String = this.fullDate().toString()

fun String.fullDate(): String? =
    ZonedDateTime.parse(this).run {
        format(
            if (this.year == ZonedDateTime.now().year) formatWithZone(DateTimeFormats.DAY_MONTH_DAY) else formatWithZone(
                DateTimeFormats.DAY_MONTH_DAY_YEAR
            )
        )
    }

fun String.hoursAndMinutes(): String =
    try {
        ZonedDateTime.parse(this).toLocalTime()
    } catch (e: DateTimeParseException) {
        LocalTime.parse(this)
    }.format(formatWithZone(DateTimeFormats.HOURS_MINUTES))
        .toUpperCase(Locale.getDefault())

fun LocalTime.hoursAndMinutes(): String =
    format(formatWithZone(DateTimeFormats.HOURS_MINUTES))
        .toUpperCase(Locale.getDefault())

fun String.dayMonth(): String =
    ZonedDateTime.parse(this).format(formatWithZone(DateTimeFormats.FULL_MONTH_DAY))

fun String.dayFullMonth(): String =
    LocalDate.parse(this.substring(0, 10)).format(formatWithZone(DateTimeFormats.FULL_MONTH_DAY))

fun String.dayShortMonth(): String =
    LocalDate.parse(substring(0, 10)).format(formatWithZone(DateTimeFormats.FULL_MONTH_DAY_SHORT))

fun String.dayOfWeek(): String =
    LocalDate.parse(substring(0, 10)).format(formatWithZone(DateTimeFormats.WEEK_DAY))

// TODO
fun formatWithZone(pattern: String): DateTimeFormatter =
    formatter(pattern).withZone(ZoneId.systemDefault())

fun formatWithZone(format: DateTimeFormats): DateTimeFormatter =
    formatWithZone(if (isCzech()) format.czech else format.english)

fun formatter(format: DateTimeFormats): DateTimeFormatter =
    formatter(if (isCzech()) format.czech else format.english)

fun formatter(pattern: String): DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

fun isCzech() = Locale.getDefault().country == "CZ"

enum class DateTimeFormats(val english: String, val czech: String = english) {
    WEEK_DAY("EEEE"),
    @Suppress("Unused") SHORT_DAY("E "),
    DAY_MONTH_DAY("EE, MMMM d", "EE d. MMMM"),
    DAY_MONTH_DAY_YEAR("EE, MMMM d, YYYY", "EE d. MMMM YYYY"),
    FULL_MONTH_DAY("MMMM d", "d. MMMM"),
    FULL_MONTH_DAY_SHORT("MMM d", "d. MMM"),
    HOURS_MINUTES("hh:mm a", "HH:mm a"),
}
