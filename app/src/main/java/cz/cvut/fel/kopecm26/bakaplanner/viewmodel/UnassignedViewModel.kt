package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class UnassignedViewModel : BaseViewModel() {

    init {
        fetchShifts()
    }

    val shifts = MutableLiveData<List<Shift>>()

    val bottomSheetVisible = MutableLiveData(false)

    fun fetchShifts() {
        working.work {
            shiftRepository.getUnassigned().parseResponse(shifts)
        }
    }
}