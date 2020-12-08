package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import kotlinx.coroutines.launch

class ScheduleViewModel : BaseViewModel() {

    val shifts = MutableLiveData<List<Shift>>()

    fun getShifts(forceUpdate: Boolean = false) {
        viewModelScope.launch {
            shiftRepository.getUpcomingShifts(forceUpdate).run {
                if (this is ResponseModel.SUCCESS) {
                    shifts.value = data
                } else if (this is ResponseModel.ERROR) {
                    errorMessage.value = errorType?.messageRes
                }
            }

        }
    }

}