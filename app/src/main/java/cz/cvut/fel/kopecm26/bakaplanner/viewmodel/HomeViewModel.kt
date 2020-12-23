package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class HomeViewModel : BaseViewModel() {
    val currentShift = MutableLiveData<Shift>()
    val nextShift = MutableLiveData<Shift?>()
    val nextWeekDays = MutableLiveData<List<Shift>>()

    init {
        getAll()
    }

    fun refreshShifts() {
        working.work {
            shiftRepository.refreshAllShifts().let(::handleRefreshResponse)
            getAll()
        }
    }

    private fun getAll() {
        working.work {
            getShifts()
            getCurrentShift()
            getNextShift()
            getNextWeekShifts()
        }
    }

    private fun handleRefreshResponse(response: ResponseModel<List<Shift>>) {
        if (response is ResponseModel.ERROR<*>) {
            errorMessage.value = response.errorType?.messageRes
        } else {
            getAll()
        }
    }

    private suspend fun getNextWeekShifts() {
        Logger.d(shiftRepository.getNextWeekShifts())
        nextWeekDays.value = shiftRepository.getNextWeekShifts()
    }

    private suspend fun getCurrentShift() {
        Logger.d(shiftRepository.getCurrentShift())
        currentShift.value = shiftRepository.getCurrentShift()
    }

    private suspend fun getNextShift() {
        Logger.d(shiftRepository.getNextShift())
        nextShift.value = shiftRepository.getNextShift()
    }

    private suspend fun getShifts() {
        shiftRepository.getCachedShifts()
    }
}