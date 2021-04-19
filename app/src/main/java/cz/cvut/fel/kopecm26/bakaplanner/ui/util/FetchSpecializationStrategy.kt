package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import java.io.Serializable

sealed class FetchSpecializationStrategy(@JvmField val isGeneral: Boolean = false): Serializable {
    object General : FetchSpecializationStrategy(true)

    class Employee(val id: Int) : FetchSpecializationStrategy()
}
