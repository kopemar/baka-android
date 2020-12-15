package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import kotlinx.coroutines.launch

class ScheduleViewModel : BaseViewModel() {

    val shifts = MutableLiveData<List<Shift>>()

    init {
        getShifts()
    }

    fun refreshShifts() {
        viewModelScope.launch {
            working.value = true
            shiftRepository.refreshAllShifts().let(::saveShifts)
            working.value = false
        }
    }

    fun getShifts() {
        viewModelScope.launch {
            working.value = true
            shiftRepository.getCachedShifts().let(::saveShifts)
            working.value = false
        }
    }

    private fun saveShifts(response: ResponseModel<List<Shift>>) {
        if (response is ResponseModel.SUCCESS) {
            Logger.d(response.data?.size)
            shifts.value = response.data
        } else if (response is ResponseModel.ERROR) {
            errorMessage.value = response.errorType?.messageRes
        }
    }

}