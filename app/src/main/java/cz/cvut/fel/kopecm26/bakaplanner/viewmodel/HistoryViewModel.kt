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
        fetchShifts(ZonedDateTime.now())
    }

    val shifts = MutableLiveData<List<Shift>>()

    fun fetchShifts(to: ZonedDateTime) = working.work {
        shiftRepository.refreshShiftsBefore(ZonedDateTime.now()).run { parseResponse(shifts) }
    }

    fun refreshShifts() {
        working.work {
            shiftRepository.refreshShiftsBefore(ZonedDateTime.now()).run { parseResponse(shifts) }
        }
    }

    private fun saveShifts(response: ResponseModel<List<Shift>>) {
        if (response is ResponseModel.SUCCESS) {
            shifts.value = response.data
        } else if (response is ResponseModel.ERROR) {
            response.errorType?.let(::parseError)
        }
    }
}