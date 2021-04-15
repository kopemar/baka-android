package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateEmployeeResponse
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.ifNotNull
import java.time.LocalDate

class AddEmployeeViewModel : BaseViewModel() {
    val firstName = MutableLiveData("")
    val lastName = MutableLiveData("")
    val username = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordRepeat = MutableLiveData("")
    // TODO
    val dateOfBirth = MutableLiveData<LocalDate>(LocalDate.of(2000, 1, 1))

    private val _response = MutableLiveData<CreateEmployeeResponse>()
    val response: LiveData<CreateEmployeeResponse> = _response

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
            firstName.value,
            lastName.value,
            username.value,
            email.value,
            password.value,
            dateOfBirth.value
        ) {
            working.work {
                userRepository.createEmployee(
                    username.value.toString(),
                    email.value.toString(),
                    password.value.toString(),
                    firstName.value.toString(),
                    lastName.value.toString(),
                    dateOfBirth.value!!
                ).parseResponse(_response)
            }
        }
    }

    // TODO email validation
    private fun notEmpty() =
        !firstName.value.isNullOrBlank() && !lastName.value.isNullOrBlank() && !username.value.isNullOrBlank() && !email.value.isNullOrBlank() && !password.value.isNullOrBlank()

}
