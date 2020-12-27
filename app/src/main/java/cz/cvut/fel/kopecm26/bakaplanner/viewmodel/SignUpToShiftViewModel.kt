package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData

class SignUpToShiftViewModel : BaseViewModel() {
    val success = MutableLiveData<Boolean?>()
}