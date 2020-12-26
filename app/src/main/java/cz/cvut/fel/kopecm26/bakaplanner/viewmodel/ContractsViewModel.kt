package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract

class ContractsViewModel : BaseViewModel() {
    val contracts = MutableLiveData<List<Contract>>()

    init {
        getContracts()
    }

    fun refreshContracts() = working.work {
        contractRepository.refreshAll().parseResponse(contracts)
    }

    private fun getContracts() {
        working.work {
            contractRepository.getAllCached().parseResponse(contracts)
        }
    }
}