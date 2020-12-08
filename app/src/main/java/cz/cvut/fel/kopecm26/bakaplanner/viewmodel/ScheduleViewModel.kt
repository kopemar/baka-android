package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import kotlinx.coroutines.launch

class ScheduleViewModel : BaseViewModel() {

    val shifts = MutableLiveData<List<Shift>>()

    init {
        getShifts()
    }

    fun getShifts(forceUpdate: Boolean = false) {

        viewModelScope.launch {
            shiftRepository.getAllShifts(forceUpdate).run {
                if (this is ResponseModel.SUCCESS) {
                    Logger.d(data?.size)
                    shifts.value = data
                } else if (this is ResponseModel.ERROR) {
                    errorMessage.value = errorType?.messageRes
                }
            }

        }
    }

}