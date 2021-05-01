package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.NotFoundError
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils

class ProfileViewModel : BaseViewModel() {
    private val _signedOut = MutableLiveData(false)
    val signedOut: LiveData<Boolean> = _signedOut

    /**
     * Call sign out. If [ResponseModel.SUCCESS] or [NotFoundError] (that means no such session exists) is returned, delete all user data.
     */
    fun signOut() {
        working.work {
            userRepository.signOut().run {
                if (this is ResponseModel.SUCCESS || (this is ResponseModel.ERROR && errorType is NotFoundError)) {
                    removeSession()
                } else if (this is ResponseModel.ERROR) {
                    errorType?.let(::parseError)
                }
            }
        }
    }

    private suspend fun removeSession() {
        PrefsUtils.remove(Constants.Prefs.USER)
        shiftRepository.deleteAll()
        _signedOut.value = true
    }
}
