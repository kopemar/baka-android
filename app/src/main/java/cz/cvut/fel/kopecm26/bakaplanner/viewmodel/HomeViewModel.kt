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
        getShifts()
        getCurrentShift()
        getNextShift()
        getNextWeekShifts()
    }

    private fun getNextWeekShifts() {
        viewModelScope.launch {
            nextWeekDays.value = shiftRepository.getNextWeekShifts()
        }
    }

    private fun getCurrentShift() {
        viewModelScope.launch {
            currentShift.value = shiftRepository.getCurrentShift()
        }
    }

    private fun getNextShift() {
        viewModelScope.launch {
            nextShift.value = shiftRepository.getNextShift()
        }
    }

    private fun getShifts() {
        viewModelScope.launch {
            shiftRepository.getAllShifts()
        }
    }
}