package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateOrganizationResponse
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.ifNotNull

class NewOrganizationViewModel : BaseViewModel() {

    val name = MutableLiveData("")

    val firstName = MutableLiveData("")
    val lastName = MutableLiveData("")
    val username = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordRepeat = MutableLiveData("")

    private val _response = MutableLiveData<CreateOrganizationResponse>()
    val response: LiveData<CreateOrganizationResponse> = _response

    private val _canSubmit = MediatorLiveData<Boolean>().apply {
        addSource(firstName) {
            value = notEmpty()
        }
        addSource(lastName) {
            value = notEmpty()
        }
        addSource(username) {
            value = notEmpty()
        }
        addSource(email) {
            value = notEmpty()
        }
        addSource(password) {
            value = notEmpty() && password.value == passwordRepeat.value
        }
        addSource(passwordRepeat) {
            value = notEmpty() && password.value == passwordRepeat.value
        }
    }

    val canSubmit: LiveData<Boolean> = _canSubmit

    fun submit() {
        ifNotNull(
            name.value,
            firstName.value,
            lastName.value,
            username.value,
            email.value,
            password.value
        ) {
            working.work {
                userRepository.signUp(
                    name.value.toString(),
                    username.value.toString(),
                    email.value.toString(),
                    password.value.toString(),
                    firstName.value.toString(),
                    lastName.value.toString()
                ).parseResponse(_response)
            }
        }
    }

    // TODO email validation
    private fun notEmpty() =
        !firstName.value.isNullOrBlank() && !lastName.value.isNullOrBlank() && !username.value.isNullOrBlank() && !email.value.isNullOrBlank() && !password.value.isNullOrBlank()

}
