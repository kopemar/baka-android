package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import kotlinx.coroutines.launch

class ProfileViewModel: BaseViewModel() {
    val signedOut = MutableLiveData(false)

    fun signOut() {
        viewModelScope.launch {
            userRepository.signOut().run {
                if (this is ResponseModel.SUCCESS) {
                    Prefs.remove(Constants.Prefs.USER)
                    shiftRepository.deleteAll()
                    signedOut.value = true
                } else if (this is ResponseModel.ERROR) {
                    errorMessage.value = errorType?.messageRes
                }
            }
        }
    }

}