package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit

class TemplateViewModel: BaseViewModel() {
    val unit = MutableLiveData<SchedulingUnit>()
    fun fetchTemplates() {

    }
}