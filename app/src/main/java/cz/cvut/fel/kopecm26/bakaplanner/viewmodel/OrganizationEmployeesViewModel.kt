package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeeListResponse
import cz.cvut.fel.kopecm26.bakaplanner.util.BasePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class OrganizationEmployeesViewModel : BaseViewModel() {

    val flow = Pager(
        PagingConfig(pageSize = 15, prefetchDistance = 1),
        pagingSourceFactory = {
            BasePagingSource {
                working.value = true
                getNextPage(it.key ?: 1).also {
                    working.value = false
                }
            }
        }
    ).flow.cachedIn(viewModelScope)

    private val _employeesState = MutableLiveData<EmployeeListResponse>()

    private val _employees = MutableSharedFlow<PagingData<Employee>>()
    val employees: Flow<PagingData<Employee>> = _employees

    fun fetchOrganizationEmployees() {
        working.work {
            fetchAndEmit(1)
        }
    }

    private suspend fun getNextPage(page: Int) = fetchAndEmit(page)

    private suspend fun fetchAndEmit(page: Int) =
        userRepository.getOrganizationEmployees(page = page).apply {
            parseResponse { _employeesState.value = it }
        }

}
