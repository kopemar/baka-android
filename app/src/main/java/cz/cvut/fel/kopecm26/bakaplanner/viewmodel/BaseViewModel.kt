package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.ViewModel
import cz.cvut.fel.kopecm26.bakaplanner.repository.UserRepository
import org.koin.java.KoinJavaComponent.inject

abstract class BaseViewModel : ViewModel() {
    val userRepository by inject(UserRepository::class.java)
}