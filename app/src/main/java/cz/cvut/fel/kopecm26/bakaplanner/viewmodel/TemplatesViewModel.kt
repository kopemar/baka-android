package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate

class TemplatesViewModel : BaseViewModel() {
    private val _unit = MutableLiveData<SchedulingUnit>()
    val unit: LiveData<SchedulingUnit> = _unit

    private val _templates = MediatorLiveData<List<ShiftTemplate>>().apply {
        addSource(_unit) { fetchTemplates() }
    }
    val templates: LiveData<List<ShiftTemplate>> = _templates

    fun setUnit(unit: SchedulingUnit) {
        if (unit != _unit.value) _unit.value = unit
    }

    fun fetchTemplates() {
        working.work {
            unit.value?.id?.let { planningRepository.getShiftTemplates(it).parseResponse(_templates) }
        }
    }
}