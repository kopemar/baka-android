package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate

class ShiftTemplateViewModel : EmployeeListViewModel() {
    init {
        Logger.d("ShiftTemplateVM")
    }
    val template = MutableLiveData<ShiftTemplate>()

    override fun fetchEmployees() {
        template.value?.id?.let {
            working.work {
                planningRepository.getShiftTemplateEmployees(it).parseResponse(_employees)
            }
        }
    }
}
