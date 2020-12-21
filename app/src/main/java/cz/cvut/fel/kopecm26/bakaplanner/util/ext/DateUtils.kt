package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

fun String.isBefore(date: ZonedDateTime = ZonedDateTime.now()): Boolean =
    ZonedDateTime.parse(this).isBefore(date)

fun String.isBefore(date: String) = isBefore(ZonedDateTime.parse(date))

fun String.daysUntilNow() = ZonedDateTime.now().until(ZonedDateTime.parse(this), ChronoUnit.DAYS)
fun String.hoursUntilNow() = ZonedDateTime.now().until(ZonedDateTime.parse(this), ChronoUnit.HOURS)
fun String.minutesUntilNow() = ZonedDateTime.now().until(ZonedDateTime.parse(this), ChronoUnit.MINUTES)

fun String.isMorning() = ZonedDateTime.parse(this).hour in (4..8)
fun String.isDay() = ZonedDateTime.parse(this).hour in (8..12)
fun String.isEvening() = ZonedDateTime.parse(this).hour in (12..16)
fun String.isNight() = ZonedDateTime.parse(this).hour.let { it in (16..24) || it in (0..4) }

fun LocalDate.weeksAfter(count: Long = 1) = plus(count, ChronoUnit.WEEKS)

fun ZonedDateTime.weeksAfter(count: Long = 1) = plus(count, ChronoUnit.WEEKS)
