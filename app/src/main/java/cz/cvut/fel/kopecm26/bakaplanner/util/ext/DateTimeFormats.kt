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

fun LocalTime.formatTime(format: DateTimeFormats): String =
    format(formatWithZone(format))

private fun formatWithZone(format: DateTimeFormats): DateTimeFormatter =
    formatWithZone(format.english)

private fun formatWithZone(pattern: String): DateTimeFormatter =
    formatter(pattern).withZone(ZoneId.systemDefault())

fun formatter(format: DateTimeFormats): DateTimeFormatter =
    formatter(format.english)

fun formatter(pattern: String): DateTimeFormatter = DateTimeFormatter.ofPattern(pattern, Locale.US)

fun isCzech() = Locale.getDefault().country == "CZ"

enum class DateTimeFormats(val english: String, val czech: String = english) {
    WEEK_DAY("EEEE"),

    @Suppress("Unused")
    SHORT_DAY("E "),
    DAY_MONTH_DAY("EE, MMMM d", "EE d. MMMM"),
    DAY_OF_MONTH("d"),
    YMD_DASH("YYYY-MM-dd"),
    DAY_MONTH_DAY_YEAR("EE, MMMM d, YYYY", "EE d. MMMM YYYY"),
    MONTH_DAY_YEAR("MMMM d, YYYY", "d. MMMM YYYY"),
    FULL_MONTH_DAY("MMMM d", "d. MMMM"),
    FULL_MONTH_DAY_SHORT("MMM d", "d. MMM"),
    HOURS_MINUTES("hh:mm a", "HH:mm a");

    companion object {
        fun getFullDateFormat(date: String) = ZonedDateTime.parse(date).run {
            if (this.year == ZonedDateTime.now().year) DAY_MONTH_DAY else DAY_MONTH_DAY_YEAR
        }
    }
}
