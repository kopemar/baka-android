package cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.isTimeBefore
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.mergeWithHours
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel

class TemplateFormViewModel : BaseViewModel() {
    val unit = MutableLiveData<SchedulingUnit>()

    val success = MutableLiveData<Boolean>()

    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()

    val start: String
        get() = unit.value?.start_time?.mergeWithHours(startTime.value.toString()).toString()

    val end: String
        get() = unit.value?.start_time?.mergeWithHours(endTime.value.toString(),
            startTime.value?.let { endTime.value?.isTimeBefore(it) } == true
        ).toString()

    fun submitTemplate() {
        working.work {
            planningRepository.addShiftTemplate(
                ShiftTemplate(
                    null,
                    start,
                    end,
                    0,
                    1
                )
            ).let(::parseResponse)
        }
    }

    fun parseResponse(response: ResponseModel<ShiftTemplate>) {
        if (response is ResponseModel.SUCCESS) {
            success.value = true
        } else if (response is ResponseModel.ERROR) {
            response.errorType?.let { parseError(it) }
        }
    }

}