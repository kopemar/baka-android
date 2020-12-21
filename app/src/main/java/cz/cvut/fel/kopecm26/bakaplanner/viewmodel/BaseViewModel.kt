package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cvut.fel.kopecm26.bakaplanner.repository.ContractRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.ShiftRepository
import cz.cvut.fel.kopecm26.bakaplanner.repository.UserRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

abstract class BaseViewModel : ViewModel() {
    protected val userRepository by inject(UserRepository::class.java)

    protected val shiftRepository by inject(ShiftRepository::class.java)

    protected val contractRepository by inject(ContractRepository::class.java)

    val errorMessage = MutableLiveData<Int>()
    val working = MutableLiveData(false)

    fun MutableLiveData<Boolean>.work(work: suspend () -> Unit) {
        viewModelScope.launch {
            this@work.value = true
            work.invoke()
            this@work.value = false
        }
    }
}