package cz.cvut.fel.kopecm26.bakaplanner.ui.forms.values

object Workload {
    val all = ArrayList<Float>().apply {
        for (i in 1..4) {
            add(i * 0.25F)
        }
    }
}