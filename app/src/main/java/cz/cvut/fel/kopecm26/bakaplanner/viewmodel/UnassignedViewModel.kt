package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class UnassignedViewModel : BaseViewModel() {

    init {
        fetchShifts()
    }

    private val _shifts = MutableLiveData<List<Shift>>()
    val shifts: LiveData<List<Shift>> = _shifts

    val bottomSheetVisible = MutableLiveData(false)

    fun fetchShifts() {
        working.work {
            shiftRepository.getUnassigned().parseResponse(_shifts)
        }
    }
}