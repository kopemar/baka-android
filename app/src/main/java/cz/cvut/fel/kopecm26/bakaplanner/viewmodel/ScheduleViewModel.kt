package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class ScheduleViewModel : BaseViewModel() {

    val shifts = MutableLiveData<List<Shift>>()

    init {
        getShifts()
    }

    fun refreshShifts() {
        working.work {
            shiftRepository.refreshAllShifts().let(::saveShifts)
        }
    }

    fun getShifts() {
        working.work {
            shiftRepository.getUpcomingShifts().let(::saveShifts)
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