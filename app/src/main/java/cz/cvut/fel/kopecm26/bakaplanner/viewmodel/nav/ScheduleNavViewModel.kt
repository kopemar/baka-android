package cz.cvut.fel.kopecm26.bakaplanner.viewmodel.nav

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel

class ScheduleNavViewModel : BaseViewModel() {
    val success = MutableLiveData<Boolean?>()
}