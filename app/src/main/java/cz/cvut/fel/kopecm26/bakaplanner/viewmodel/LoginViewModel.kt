package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ErrorType
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.UnauthorizedError
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.User
import cz.cvut.fel.kopecm26.bakaplanner.util.SingleLiveEvent
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils

class LoginViewModel : BaseViewModel() {
    private val _signedIn = SingleLiveEvent<Boolean>()
    val signedIn: LiveData<Boolean> = _signedIn

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
            _signedIn.value = true
            PrefsUtils.saveUser(response.data)
        } else if (response is ResponseModel.ERROR) {
            response.errorType?.let(::parseError)
        }
    }

    override fun parseError(error: ErrorType) {
        if (error is UnauthorizedError) this._errorMessage.value = R.string.wrong_password
        else super.parseError(error)
    }
}
