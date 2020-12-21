package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    val currentShift = MutableLiveData<Shift>()
    val nextShift = MutableLiveData<Shift?>()
    val nextWeekDays = MutableLiveData<List<Shift>>()

    init {
        getAll()
    }

    fun refreshShifts() {
        viewModelScope.launch {
            shiftRepository.refreshAllShifts()
            getAll()
        }
    }

    fun getAll() {
        viewModelScope.launch {
            working.value = true
            getShifts()
            getCurrentShift()
            getNextShift()
            getNextWeekShifts()
            working.value = false
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