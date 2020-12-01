package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import com.orhanobut.logger.Logger

class MainViewModel : BaseViewModel() {

    suspend fun signIn(username: String, password: String) = userRepository.signIn(username, password)

    fun init() {
        Logger.d("hello world")
    }
}