package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

fun String.isBefore(date: ZonedDateTime = ZonedDateTime.now()): Boolean = ZonedDateTime.parse(this).isBefore(date)

fun String.isBefore(date: String) = isBefore(ZonedDateTime.parse(date))

fun LocalDate.weeksAfter(count: Long = 1) = plus(count, ChronoUnit.WEEKS)
