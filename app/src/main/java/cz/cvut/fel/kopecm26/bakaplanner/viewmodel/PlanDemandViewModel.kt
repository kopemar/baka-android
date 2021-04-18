package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.util.SingleSelectionList

class PlanDemandViewModel : BaseViewModel() {
    private val _daySelection = MutableLiveData<SingleSelectionList<Selection<PeriodDay>>>()
    val daySelection: LiveData<SingleSelectionList<Selection<PeriodDay>>> = _daySelection

    private val _units = MutableLiveData<List<ShiftTemplate>>()

    private val _unitsMap = MediatorLiveData<Map<String, List<ShiftTemplate>>>().apply {
        addSource(_units) {
            value = it.groupBy { item -> "${item.startTimeHours} – ${item.endTimeHours}" }
        }
    }

    val units: LiveData<Map<String, List<ShiftTemplate>>> = _unitsMap

    fun fetchUnits(periodId: Int) {
        working.work {
            planningRepository.getSchedulingUnits(periodId).parseResponse { response ->
                _daySelection.value = SingleSelectionList<Selection<PeriodDay>>().apply {
                    response?.map { Selection(it.toPeriodDay()) }?.let { addAll(it) }
                    this.firstOrNull()?.selected = true
                    this.firstOrNull()?.item?.id?.let { fetchShiftsInUnit(it) }
                }
            }
        }
    }

    fun fetchShiftsInUnit(unitId: Int) {
        working.work {
            planningRepository.getShiftTemplates(unitId).parseResponse(_units)
        }
    }
}
