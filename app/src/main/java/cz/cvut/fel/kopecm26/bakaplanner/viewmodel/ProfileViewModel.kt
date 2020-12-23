package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import com.pixplicity.easyprefs.library.Prefs
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.NotFoundError
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.util.Constants

class ProfileViewModel: BaseViewModel() {
    val signedOut = MutableLiveData(false)

    /**
     * Call sign out. If [ResponseModel.SUCCESS] or [NotFoundError] (that means no such session exists) is returned, delete all user data.
     */
    fun signOut() {
        working.work {
            userRepository.signOut().run {
                if (this is ResponseModel.SUCCESS) {
                    removeSession()
                } else if (this is ResponseModel.ERROR) {
                    if (this.errorType is NotFoundError) removeSession()
                    else errorMessage.value = errorType?.messageRes
                }
            }
        }
    }

    private suspend fun removeSession() {
        Prefs.remove(Constants.Prefs.USER)
        shiftRepository.deleteAll()
        contractRepository.deleteAll()
        signedOut.value = true
    }
}