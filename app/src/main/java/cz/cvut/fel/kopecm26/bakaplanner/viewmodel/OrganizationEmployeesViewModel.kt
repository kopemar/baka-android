package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeeListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class OrganizationEmployeesViewModel : BaseViewModel() {

    val flow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 10)
    ) {
        EmployeePagingSource()
    }.flow.cachedIn(viewModelScope)

    private val _employeesState = MutableLiveData<EmployeeListResponse>()
//    val employees: Flow<PagingData<Employee>> = _employees

    private val _employees = MutableSharedFlow<PagingData<Employee>>()
    val employees: Flow<PagingData<Employee>> = _employees

    fun fetchOrganizationEmployees() {
        working.work {
            fetchAndEmit(1)
        }
    }

    suspend fun getNextPage(page: Int) = fetchAndEmit(page) as? ResponseModel.SUCCESS

    private suspend fun fetchAndEmit(page: Int) =
        userRepository.getOrganizationEmployees(page = page).apply {
            parseResponse { _employeesState.value = it }
        }


    inner class EmployeePagingSource : PagingSource<Int, Employee>() {

        override fun getRefreshKey(state: PagingState<Int, Employee>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                val anchorPage = state.closestPageToPosition(anchorPosition)
                anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            }
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Employee> {
            val response = getNextPage(params.key ?: 1)
            return LoadResult.Page(
                data = response?.data?.data ?: listOf(),
                prevKey = null,
                nextKey = if (response?.data?.has_next == true) params.key?.plus(1) else null,
            )
        }
    }
}
