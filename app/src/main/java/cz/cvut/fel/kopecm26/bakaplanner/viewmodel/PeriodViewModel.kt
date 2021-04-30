package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.PeriodDay
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.util.SingleSelectionList

class PeriodViewModel : BaseViewModel() {

    private val _daySelection = MutableLiveData<SingleSelectionList<Selection<PeriodDay>>>()
    val daySelection: LiveData<SingleSelectionList<Selection<PeriodDay>>> = _daySelection

    private val _templates = MutableLiveData<List<ShiftTemplate>>()
    val templates: LiveData<List<ShiftTemplate>> = _templates

    private val _period = MutableLiveData<SchedulingPeriod>()
    val period: LiveData<SchedulingPeriod> = _period
    fun setPeriod(period: SchedulingPeriod) {
        if (_period.value != period) _period.value = period
    }

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    private val _units = MediatorLiveData<List<SchedulingUnit>>().apply {
        addSource(_period) { fetchSchedulingUnits() }
    }
    val units: LiveData<List<SchedulingUnit>> = _units

    fun fetchSchedulingUnits() {
        working.work {
            _period.value?.id?.let { id ->
                planningRepository.getSchedulingUnits(id).parseResponse { response ->
                    _daySelection.value = SingleSelectionList<Selection<PeriodDay>>().apply {
                        response?.map { Selection(it.toPeriodDay()) }?.let { addAll(it) }
                        this.firstOrNull()?.selected = true
                        this.firstOrNull()?.item?.id?.let { fetchShiftsInUnit(it) }
                    }
                    _units.value = response
                }
            }
        }
    }

    fun fetchShiftsInUnit(id: Int) {
        working.work {
            planningRepository.getShiftTemplates(id).parseResponse(_templates)
        }
    }

    fun submit() {
        working.work {
            _period.value?.id?.let { id ->
                planningRepository.submitSchedule(id).parseResponse {
                    _period.value = it
                    _success.value = true
                }
            }
        }
    }
}
