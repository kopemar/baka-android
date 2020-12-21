package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

class HistoryViewModel : BaseViewModel() {

    init {
        Logger.d(LocalDateTime.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
        getShifts(ZonedDateTime.now())
    }

    val shifts = MutableLiveData<List<Shift>>()

    fun getShifts(to: ZonedDateTime) = working.work {
        shiftRepository.getShiftsBefore(to).let(::saveShifts)
    }

    fun refreshShifts() {
        working.work {
            shiftRepository.refreshShiftsBefore(ZonedDateTime.now()).let(::saveShifts)
        }
    }

    private fun saveShifts(response: ResponseModel<List<Shift>>) {
        if (response is ResponseModel.SUCCESS) {
            shifts.value = response.data
        } else if (response is ResponseModel.ERROR) {
            errorMessage.value = response.errorType?.messageRes
        }
    }
}