package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class ShiftViewModel: BaseViewModel() {
    val shift = MutableLiveData<Shift>()

    fun getShift(id: Int) {
        working.work {
            shiftRepository.getShift(id).run {
                if (this is ResponseModel.SUCCESS)
                    shift.value = this.data
                else if (this is ResponseModel.ERROR) {
                    errorMessage.value = this.errorType?.messageRes
                }
            }
        }
    }
}