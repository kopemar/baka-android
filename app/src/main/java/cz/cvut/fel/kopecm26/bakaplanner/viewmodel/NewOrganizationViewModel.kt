package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
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

}
