package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import java.io.Serializable

sealed class FetchEmployeesStrategy : Serializable {

    object General : FetchEmployeesStrategy()

    class Specialization(val id: Int) : FetchEmployeesStrategy()

    class Template(val id: Int) : FetchEmployeesStrategy()

}

