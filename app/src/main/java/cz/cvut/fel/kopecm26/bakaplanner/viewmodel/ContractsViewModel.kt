package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchContractsStrategy

class ContractsViewModel : BaseViewModel() {
    private val _contracts = MutableLiveData<List<Contract>>()
    val contracts: LiveData<List<Contract>> = _contracts

    private val _strategy = MutableLiveData<FetchContractsStrategy>()
    val strategy: LiveData<FetchContractsStrategy> = _strategy

    fun setStrategy(type: FetchContractsStrategy) {
        _strategy.value = type
        fetchOrganizationSpecializations(type)
    }

    private fun fetchOrganizationSpecializations(type: FetchContractsStrategy) {
        working.work {
            when (type) {
                is FetchContractsStrategy.Employee -> contractRepository.getEmployeeContracts(type.id)
                else -> contractRepository.getContracts()
            }.parseResponse(_contracts)
        }
    }

    fun refreshContracts() {
        _strategy.value?.let(::fetchOrganizationSpecializations)
    }

}
