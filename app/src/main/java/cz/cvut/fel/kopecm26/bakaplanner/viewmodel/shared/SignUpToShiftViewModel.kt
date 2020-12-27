package cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel

class SignUpToShiftViewModel : BaseViewModel() {
    val success = MutableLiveData<Boolean?>()
}