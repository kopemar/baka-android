package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cz.cvut.fel.kopecm26.bakaplanner.repository.ContractRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.ShiftRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.UserRepository
import org.koin.java.KoinJavaComponent.inject

abstract class BaseViewModel : ViewModel() {
    protected val userRepository by inject(UserRepository::class.java)

    protected val shiftRepository by inject(ShiftRepository::class.java)

    protected val contractRepository by inject(ContractRepository::class.java)

    val errorMessage = MutableLiveData<Int>()
    val working = MutableLiveData(false)
}