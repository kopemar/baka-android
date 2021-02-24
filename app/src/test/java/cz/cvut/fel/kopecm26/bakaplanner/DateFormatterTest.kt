package cz.cvut.fel.kopecm26.bakaplanner

import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hoursAndMinutes
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.mergeWithHours
import org.junit.Assert
import org.junit.Test

class DateFormatterTest {

    @Test
    fun hoursMinutes() {
        Assert.assertEquals("11:00 AM", "2020-11-13T11:00:00.000Z".hoursAndMinutes())
    }

    @Test
    fun merge() {
        Assert.assertEquals("2020-11-13T11:30+01:00[Europe/Prague]", "2020-11-13T11:00:00.000Z".mergeWithHours("11:30", false))
        Assert.assertEquals("2020-11-14T12:30+01:00[Europe/Prague]", "2020-11-13T11:00:00.000Z".mergeWithHours("12:30", true))
    }
}
