package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.paging.PagingData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import kotlinx.coroutines.flow.Flow

abstract class EmployeeListViewModel: BaseViewModel() {

    abstract val flow: Flow<PagingData<Employee>>

}
