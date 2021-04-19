package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import java.io.Serializable

sealed class FetchContractsStrategy : Serializable {

    object General : FetchContractsStrategy()

    class Employee(val id: Int) : FetchContractsStrategy()

}
