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
        PagingConfig(pageSize = 15, prefetchDistance = 1),
        pagingSourceFactory = { EmployeePagingSource() }
    ).flow.cachedIn(viewModelScope)

    private val _employeesState = MutableLiveData<EmployeeListResponse>()
//    val employees: Flow<PagingData<Employee>> = _employees

    private val _employees = MutableSharedFlow<PagingData<Employee>>()
    val employees: Flow<PagingData<Employee>> = _employees

    fun fetchOrganizationEmployees() {
        working.work {
            fetchAndEmit(1)
        }
    }

    suspend fun getNextPage(page: Int) = fetchAndEmit(page)

    private suspend fun fetchAndEmit(page: Int) =
        userRepository.getOrganizationEmployees(page = page).apply {
            parseResponse { _employeesState.value = it }
        }


    inner class EmployeePagingSource : PagingSource<Int, Employee>() {

        override fun getRefreshKey(state: PagingState<Int, Employee>): Int? = state.anchorPosition

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Employee> {
            working.value = true
            val response = getNextPage(params.key ?: 1)
            working.value = false
            return if (response is ResponseModel.SUCCESS) {
                LoadResult.Page(
                    data = response.data?.data ?: listOf(),
                    prevKey = null,
                    nextKey = if (response.data?.hasNext == true) _employeesState.value?.currentPage?.plus(1) ?: 1 else null,
                )
            } else {
                LoadResult.Error(
                    RuntimeException()
                )
            }
        }
    }
}
