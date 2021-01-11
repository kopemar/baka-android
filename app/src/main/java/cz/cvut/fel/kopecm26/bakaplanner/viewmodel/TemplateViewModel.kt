package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate

class TemplateViewModel : BaseViewModel() {
    val unit = MutableLiveData<SchedulingUnit>()

    private val _templates = MutableLiveData<List<ShiftTemplate>>()
    val templates: LiveData<List<ShiftTemplate>> = _templates

    fun fetchTemplates() {
        working.work {
            unit.value?.id?.let { planningRepository.getShiftTemplates(it).parseResponse(_templates) }
        }
    }
}