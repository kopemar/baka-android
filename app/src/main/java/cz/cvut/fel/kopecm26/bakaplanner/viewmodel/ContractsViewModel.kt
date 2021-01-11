package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract

class ContractsViewModel : BaseViewModel() {
    private val _contracts = MutableLiveData<List<Contract>>()
    val contracts: LiveData<List<Contract>> = _contracts

    init {
        getContracts()
    }

    fun refreshContracts() = working.work {
        contractRepository.refreshAll().parseResponse(_contracts)
    }

    private fun getContracts() {
        working.work {
            contractRepository.getAllCached().parseResponse(_contracts)
        }
    }
}