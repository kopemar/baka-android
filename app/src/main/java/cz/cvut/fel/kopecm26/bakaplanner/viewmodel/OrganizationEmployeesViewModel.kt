package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Employee
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.util.BasePagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class OrganizationEmployeesViewModel : BaseViewModel() {

    val flow = Pager(
        PagingConfig(pageSize = 15, prefetchDistance = 1),
        pagingSourceFactory = {
            BasePagingSource {
                working.value = true
                getNextPage(it.key ?: 1).also { response ->
                    working.value = false
                    setShowEmptyResource(response is ResponseModel.SUCCESS && response.data?.data.isNullOrEmpty())
                }
            }
        }
    ).flow.cachedIn(viewModelScope)

    private val _employees = MutableSharedFlow<PagingData<Employee>>()
    val employees: Flow<PagingData<Employee>> = _employees

    private suspend fun getNextPage(page: Int) = fetchAndEmit(page)

    private suspend fun fetchAndEmit(page: Int) =
        userRepository.getOrganizationEmployees(page = page)

}
