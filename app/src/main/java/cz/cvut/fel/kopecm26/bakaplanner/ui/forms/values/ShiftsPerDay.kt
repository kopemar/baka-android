package cz.cvut.fel.kopecm26.bakaplanner.ui.forms.values

object ShiftsPerDay {
    val all = ArrayList<Int>().apply {
        for (i in 1..5) {
            add(i)
        }
    }
}
