package cz.cvut.fel.kopecm26.bakaplanner.ui.forms.values

object Break {
    val breakMinutes = ArrayList<Int>().apply {
        for (i in 0..100 step 5) {
            add(i)
        }
    }
}
