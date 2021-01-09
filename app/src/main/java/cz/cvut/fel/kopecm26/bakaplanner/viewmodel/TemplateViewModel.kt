package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate

class TemplateViewModel : BaseViewModel() {
    val unit = MutableLiveData<SchedulingUnit>()
    val templates = MutableLiveData<List<ShiftTemplate>>()

    fun fetchTemplates() {
        working.work {
            unit.value?.id?.let { planningRepository.getShiftTemplates(it).parseResponse(templates) }
        }
    }
}