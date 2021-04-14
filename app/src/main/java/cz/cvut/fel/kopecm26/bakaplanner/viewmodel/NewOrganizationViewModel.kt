package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

class NewOrganizationViewModel : BaseViewModel() {

    val name = MutableLiveData("")

    val firstName = MutableLiveData("")
    val lastName = MutableLiveData("")
    val username = MutableLiveData("")
    val email = MutableLiveData("")
    val password = MutableLiveData("")
    val passwordRepeat = MutableLiveData("")

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

    // TODO email validation
    private fun notEmpty() =
        !firstName.value.isNullOrBlank() && !lastName.value.isNullOrBlank() && !username.value.isNullOrBlank() && !email.value.isNullOrBlank() && !password.value.isNullOrBlank()

    val canSubmit: LiveData<Boolean> = _canSubmit

}
