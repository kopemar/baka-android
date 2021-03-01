package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User

class ShiftTemplateViewModel : BaseViewModel() {
    init {
        Logger.d("ShiftTemplateVM")
    }
    val template = MutableLiveData<ShiftTemplate>()

    private val _employees = MediatorLiveData<List<User>>().apply {
        addSource(template) {
            Logger.d(template)
            fetchEmployees()
        }
    }

    val employees: LiveData<List<User>> = _employees

    fun fetchEmployees() {
        template.value?.id?.let {
            working.work {
                planningRepository.getShiftTemplateEmployees(it).parseResponse(_employees)
            }
        }
    }
}
