package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

fun String.isBefore(date: ZonedDateTime = ZonedDateTime.now()): Boolean =
    try {
        ZonedDateTime.parse(this)
    } catch (e: DateTimeParseException) {
        LocalDate.parse(this).atStartOfDay(ZoneId.systemDefault())
    }.isBefore(date)

fun String.isBefore(date: String) = isBefore(
    try {
        ZonedDateTime.parse(date)
    } catch (e: DateTimeParseException) {
        LocalDate.parse(date).atStartOfDay(ZoneId.systemDefault())
    }
)

fun String.daysUntilNow() = ZonedDateTime.now().until(ZonedDateTime.parse(this), ChronoUnit.DAYS)
fun String.hoursUntilNow() = ZonedDateTime.now().until(ZonedDateTime.parse(this), ChronoUnit.HOURS)
fun String.minutesUntilNow() =
    ZonedDateTime.now().until(ZonedDateTime.parse(this), ChronoUnit.MINUTES)

fun String.isMorning() = try {
    ZonedDateTime.parse(this).toLocalTime()
} catch (e: DateTimeParseException) {
    LocalTime.parse(this)
}.hour in (4..8)

fun String.isDay() = try {
    ZonedDateTime.parse(this).toLocalTime()
} catch (e: DateTimeParseException) {
    LocalTime.parse(this)
}.hour in (8..12)

fun String.isEvening() = try {
    ZonedDateTime.parse(this).toLocalTime()
} catch (e: DateTimeParseException) {
    LocalTime.parse(this)
}.hour in (12..16)

fun String.isNight() = ZonedDateTime.parse(this).hour.let { it in (16..24) || it in (0..4) }

fun String.getHour() = try {
    LocalTime.parse(this, formatter("HH:mm")).hour
} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun String.getMinute() = try {
    LocalTime.parse(this, formatter("HH:mm")).minute
} catch (e: Exception) {
    e.printStackTrace()
    null
}

fun String.mergeWithHours(time: String, nextDay: Boolean = false): String {
    val mergedDate = ZonedDateTime.parse(this)
    val mergedTime = LocalTime.parse(time)
    return ZonedDateTime.of(
        mergedDate.year,
        mergedDate.monthValue,
        mergedDate.dayOfMonth,
        mergedTime.hour,
        mergedTime.minute,
        0,
        0,
        ZoneId.systemDefault()
    ).run { if (nextDay) this.plusDays(1) else this }.toString()
}

fun ZonedDateTime.mergeWithHours(time: String, nextDay: Boolean = false): String {
    val mergedDate = this
    val mergedTime = LocalTime.parse(time)
    return ZonedDateTime.of(
        mergedDate.year,
        mergedDate.monthValue,
        mergedDate.dayOfMonth,
        mergedTime.hour,
        mergedTime.minute,
        0,
        0,
        ZoneId.systemDefault()
    ).run { if (nextDay) this.plusDays(1) else this }.toString()
}

fun LocalDate.weeksAfter(count: Long = 1) = plus(count, ChronoUnit.WEEKS)

fun ZonedDateTime.weeksAfter(count: Long = 1) = plus(count, ChronoUnit.WEEKS)

fun String.isTimeBefore(second: String) = LocalTime.parse(this).isBefore(LocalTime.parse(second))
