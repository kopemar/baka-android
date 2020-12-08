package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    val currentShift = MutableLiveData<Shift>()
    val nextShift = MutableLiveData<Shift>()
    val nextWeekDays = MutableLiveData<List<Shift>>()

    init {
        getAll()
    }


    fun getAll(force: Boolean = false) {
        viewModelScope.launch {
            working.value = true
            getShifts(force)
            getCurrentShift()
            getNextShift()
            getNextWeekShifts()
            working.value = false
        }
    }

    private suspend fun getNextWeekShifts() {
        nextWeekDays.value = shiftRepository.getNextWeekShifts()
    }

    private suspend fun getCurrentShift() {
        currentShift.value = shiftRepository.getCurrentShift()
    }

    private suspend fun getNextShift() {
        nextShift.value = shiftRepository.getNextShift()
    }

    private suspend fun getShifts(force: Boolean = false) {
        shiftRepository.getAllShifts(force)
    }
}