package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift

class ShiftViewModel: BaseViewModel() {
    val shift = MutableLiveData<Shift>()

    fun fetchShift(id: Int) {
        working.work {
            shiftRepository.getShift(id).run {
                parseResponse(shift)
            }
        }
    }
}