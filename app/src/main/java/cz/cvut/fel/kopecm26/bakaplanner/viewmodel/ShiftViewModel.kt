package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import kotlinx.coroutines.launch

class ShiftViewModel: BaseViewModel() {
    val shift = MutableLiveData<Shift>()

    fun getShift(id: Int) {
        viewModelScope.launch {
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