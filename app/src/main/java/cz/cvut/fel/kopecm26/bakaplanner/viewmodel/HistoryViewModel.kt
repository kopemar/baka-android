package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.temporal.TemporalAdjusters

class HistoryViewModel: BaseViewModel() {

    init {
        Logger.d(LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
        getShifts(LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)), LocalDateTime.now())
    }

    val shifts = MutableLiveData<List<Shift>>()

    fun getShifts(from: LocalDateTime, to: LocalDateTime) {
        viewModelScope.launch {
            shiftRepository.getCachedShifts(from, to).let(::saveShifts)
        }
    }

    private fun saveShifts(response: ResponseModel<List<Shift>>) {
        if (response is ResponseModel.SUCCESS) {
            Logger.d(response.data?.size)
            shifts.value = response.data
        } else if (response is ResponseModel.ERROR) {
            errorMessage.value = response.errorType?.messageRes
        }
    }
}