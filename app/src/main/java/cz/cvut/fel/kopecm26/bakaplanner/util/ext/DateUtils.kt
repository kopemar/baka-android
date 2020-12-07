package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import java.time.ZonedDateTime

fun String.isBefore(date: ZonedDateTime = ZonedDateTime.now()): Boolean = ZonedDateTime.parse(this).isBefore(date)

fun String.isBefore(date: String) = isBefore(ZonedDateTime.parse(date))
