package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel

class ContractsViewModel : BaseViewModel() {
    val contracts = MutableLiveData<List<Contract>>()

    init {
        getContracts()
    }

    fun refreshContracts() = working.work {
        contractRepository.refreshAll().let(::saveContracts)
    }

    private fun getContracts() {
        working.work {
            contractRepository.getAllCached().let(::saveContracts)
        }
    }

    private fun saveContracts(response: ResponseModel<List<Contract>>) {
        if (response is ResponseModel.SUCCESS) {
            Logger.d(response.data?.size)
            contracts.value = response.data
        } else if (response is ResponseModel.ERROR) {
            errorMessage.value = response.errorType?.messageRes
        }
    }
}