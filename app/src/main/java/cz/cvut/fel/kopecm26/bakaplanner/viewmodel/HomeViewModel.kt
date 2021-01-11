package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class HomeViewModel : BaseViewModel() {
    private val _currentShift = MutableLiveData<Shift>()
    val currentShift: LiveData<Shift> = _currentShift

    private val _nextShift = MutableLiveData<Shift>()
    val nextShift: LiveData<Shift?> = _nextShift

    private val _nextWeekDays = MutableLiveData<List<Shift>>()
    val nextWeekDays: LiveData<List<Shift>> = _nextWeekDays

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
            response.errorType?.let(::parseError)
        } else {
            getAll()
        }
    }

    private suspend fun getNextWeekShifts() {
        Logger.d(shiftRepository.getNextWeekShifts())
        _nextWeekDays.value = shiftRepository.getNextWeekShifts()
    }

    private suspend fun getCurrentShift() {
        Logger.d(shiftRepository.getCurrentShift())
        _currentShift.value = shiftRepository.getCurrentShift()
    }

    private suspend fun getNextShift() {
        Logger.d(shiftRepository.getNextShift())
        _nextShift.value = shiftRepository.getNextShift()
    }

    private suspend fun getShifts() {
        shiftRepository.getCachedShifts()
    }
}