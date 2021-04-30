package cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel

class SharedViewModel : BaseViewModel() {
    val signUpSuccess = MutableLiveData<Consumable<Boolean>>()

    val removeSuccess = MutableLiveData<Consumable<Boolean>>()

    val assignSuccess = MutableLiveData<Consumable<Boolean>>()

    val setDemandSuccess = MutableLiveData<Consumable<Boolean>>()

    val periodChanged = MutableLiveData<Consumable<Boolean>>()
}
