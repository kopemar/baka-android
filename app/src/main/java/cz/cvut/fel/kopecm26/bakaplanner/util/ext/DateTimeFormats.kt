package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

// todo
fun String.formatDate(format: DateTimeFormats): String =
    try {
        getLocalDate().format(formatWithZone(format))
    } catch (e: DateTimeParseException) {
        this
    }

fun String.formatTime(format: DateTimeFormats): String =
    try {
        getLocalDateTime().format(formatWithZone(format))
    } catch (e: DateTimeParseException) {
        this
    }.run {
        if (format == DateTimeFormats.HOURS_MINUTES) toUpperCase(Locale.getDefault()) else this
    }

fun String.getLocalDate(): LocalDate =
    try {
        ZonedDateTime.parse(this).toLocalDate()
    } catch (e: DateTimeParseException) {
        LocalDate.parse(this.substring(0, 10))
    }

fun String.getLocalDateTime(): LocalDateTime =
    try {
        ZonedDateTime.parse(this).toLocalDateTime()
    } catch (e: DateTimeParseException) {
        LocalDateTime.parse(this)
    }

fun String.fullDate(): String? =
    ZonedDateTime.parse(this).run {
        format(
            if (this.year == ZonedDateTime.now().year) formatWithZone(DateTimeFormats.DAY_MONTH_DAY) else formatWithZone(
                DateTimeFormats.DAY_MONTH_DAY_YEAR
            )
        )
    }

fun LocalTime.formatTime(format: DateTimeFormats): String =
    format(formatWithZone(format))

fun String.dayShortMonth(): String =
    getLocalDate().format(formatWithZone(DateTimeFormats.FULL_MONTH_DAY_SHORT))

fun String.dayOfWeek(): String =
    getLocalDate().format(formatWithZone(DateTimeFormats.WEEK_DAY))

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

    @Suppress("Unused")
    SHORT_DAY("E "),
    DAY_MONTH_DAY("EE, MMMM d", "EE d. MMMM"),
    DAY_MONTH_DAY_YEAR("EE, MMMM d, YYYY", "EE d. MMMM YYYY"),
    FULL_MONTH_DAY("MMMM d", "d. MMMM"),
    FULL_MONTH_DAY_SHORT("MMM d", "d. MMM"),
    HOURS_MINUTES("hh:mm a", "HH:mm a"),
}
