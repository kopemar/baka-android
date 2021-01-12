package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate

class ShiftTemplateViewModel: BaseViewModel() {
    val template = MutableLiveData<ShiftTemplate>()
}