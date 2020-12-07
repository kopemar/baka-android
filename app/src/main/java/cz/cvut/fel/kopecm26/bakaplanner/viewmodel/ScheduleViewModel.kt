package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import kotlinx.coroutines.launch

class ScheduleViewModel : BaseViewModel() {

    val shifts = MutableLiveData<List<Shift>>()
    val showError = MutableLiveData<Int>()

    fun getShifts() {
        viewModelScope.launch {
            shiftRepository.getShifts().run {
                if (this is ResponseModel.SUCCESS) {
                    shifts.postValue(data)
                } else if (this is ResponseModel.ERROR) {
                    showError.postValue(errorType?.messageRes)
                }
            }

        }
    }

}