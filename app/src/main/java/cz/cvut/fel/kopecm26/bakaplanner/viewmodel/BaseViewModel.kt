package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ErrorType
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.NoInternetError
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.NoServerConnectionError
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
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
    val noNetworkConnection = MutableLiveData(false)
    val working = MutableLiveData(false)

    fun MutableLiveData<Boolean>.work(work: suspend () -> Unit) {
        viewModelScope.launch {
            this@work.value = true
            work.invoke()
            this@work.value = false
        }
    }

    protected fun parseError(error: ErrorType) {
        if (error is NoInternetError || error is NoServerConnectionError) {
            Logger.d("No Internet Error")
            noNetworkConnection.value = true
        }
        errorMessage.value = error.messageRes
    }

    protected fun <T> ResponseModel<T>.parseResponse(liveData: MutableLiveData<T>) {
        if (this is ResponseModel.SUCCESS) liveData.value = data
        else if (this is ResponseModel.ERROR) errorType?.let(::parseError)
    }
}