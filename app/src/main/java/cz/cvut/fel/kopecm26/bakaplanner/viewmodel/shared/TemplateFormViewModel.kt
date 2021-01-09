package cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel

class TemplateFormViewModel: BaseViewModel() {
    val unit = MutableLiveData<SchedulingUnit>()

    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()
}