package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class PlanDaysViewModel: BaseViewModel() {
    val shiftHours = MutableLiveData<Int?>()

    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()

    val breakMinutes = MediatorLiveData<Int>().apply {
        this.addSource(shiftHours) {
            if (it != null && this.value == null) this.value = 30
        }
    }

    val shiftsPerDay = MutableLiveData(1)
}