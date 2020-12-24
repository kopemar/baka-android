package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class UnassignedViewModel: BaseViewModel() {

    init {
        getShifts()
    }

    val shifts = MutableLiveData<List<Shift>>()

    fun getShifts() {
        working.work {
            shiftRepository.getUnassigned().let(::saveShifts)
        }
    }

    // TODO make more generic...
    private fun saveShifts(response: ResponseModel<List<Shift>>) {
        if (response is ResponseModel.SUCCESS) {
            shifts.value = response.data
        } else if (response is ResponseModel.ERROR) {
            errorMessage.value = response.errorType?.messageRes
        }
    }
}