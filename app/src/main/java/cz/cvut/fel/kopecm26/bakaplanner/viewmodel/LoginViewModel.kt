package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData

class LoginViewModel: BaseViewModel() {
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    suspend fun signIn() = username.value?.let { userRepository.signIn(it, password.value.toString()) }
}