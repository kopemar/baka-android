package cz.cvut.fel.kopecm26.bakaplanner.ui.forms.values

object ShiftHours {
    val all = ArrayList<Int>().apply {
        for (i in 8..12) {
            add(i)
        }
    }
}
