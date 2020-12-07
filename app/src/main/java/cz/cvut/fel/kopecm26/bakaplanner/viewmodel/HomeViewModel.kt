package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import kotlinx.coroutines.launch

class HomeViewModel : BaseViewModel() {
    val currentShift = MutableLiveData<Shift>()
    val nextShift = MutableLiveData<Shift>()

    init {
        getCurrentShift()
        getNextShift()
    }

    fun getCurrentShift() {
        viewModelScope.launch {
            currentShift.value = shiftRepository.getCurrentShift()
        }
    }

    fun getNextShift() {
        viewModelScope.launch {
            nextShift.value = shiftRepository.getNextShift()
        }
    }
}