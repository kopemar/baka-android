package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.util.SingleLiveEvent
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils

class LoginViewModel : BaseViewModel() {
    val signedIn = SingleLiveEvent<Boolean>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    fun signIn() {
        working.work {
            username.value?.let {
                userRepository.signIn(
                    it,
                    password.value.toString()
                ).let(::handleLoginResult)
            }
        }
    }

    private fun handleLoginResult(response: ResponseModel<User>) {
        if (response is ResponseModel.SUCCESS) {
            signedIn.value = true
            PrefsUtils.saveUser(response.data)
        } else if (response is ResponseModel.ERROR) {
            response.errorType?.let(::parseError)
        }
    }
}