package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ContractTypes
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateContractResponse
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.DateTimeFormats
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.formatter
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.ifNotNull
import java.time.LocalDate

class AddContractViewModel: BaseViewModel() {
    private val _response = MutableLiveData<CreateContractResponse>()
    val response: LiveData<CreateContractResponse> = _response

    private val _employee = MutableLiveData<Int>()
    fun setEmployee(employee: Int) {
        _employee.value = employee
    }

    private val _contractType = MutableLiveData(ContractTypes.EMPLOYMENT_CONTRACT)
    val contractType: LiveData<ContractTypes> = _contractType
    fun setContractType(type: ContractTypes) {
        _contractType.value = type
    }


    private val _workLoad = MutableLiveData(1F)
    private val _workLoadF = MediatorLiveData<String>().apply {
        addSource(_workLoad) { value = it.toString() }
    }
    val workLoadF: LiveData<String> = _workLoadF
    fun setWorkLoad(value: Float) {
        _workLoad.value = value
    }


    private val _startDate = MutableLiveData<LocalDate>()
    val startDate: LiveData<LocalDate> = _startDate
    fun setStartDate(date: LocalDate) {
        _startDate.value = date
    }
    private val _startDateFormatted = MediatorLiveData<String>().apply {
        addSource(_startDate) {
            value = it.format(formatter(DateTimeFormats.MONTH_DAY_YEAR))
        }
    }
    val startDateFormatted: LiveData<String> = _startDateFormatted


    private val _endDate = MutableLiveData<LocalDate>()
    val endDate: LiveData<LocalDate> = _endDate
    fun setEndDate(date: LocalDate) {
        _endDate.value = date
    }

    private val _endDateFormatted = MediatorLiveData<String>().apply {
        addSource(_endDate) {
            value = it.format(formatter(DateTimeFormats.MONTH_DAY_YEAR))
        }
    }
    val endDateFormatted: LiveData<String> = _endDateFormatted

    private val _indefinite = MutableLiveData(false)
    val indefinite: LiveData<Boolean> = _indefinite
    fun setIndefinite(value: Boolean) {
        _indefinite.value = value
    }

    fun submit() {
        Logger.d("end_time: ${_workLoad.value}")
        ifNotNull(
            _employee.value,
            _contractType.value,
            _startDate.value,
            _indefinite.value,
            if (_contractType.value == ContractTypes.EMPLOYMENT_CONTRACT) _workLoad.value else "",
            if (_indefinite.value == true) "" else _endDate.value
        ) {
            working.work {
                contractRepository.createContract(
                    _employee.value!!,
                    startDate.value.toString(),
                    endDate = if (_indefinite.value == true) null else _endDate.value.toString(),
                    if (_contractType.value == ContractTypes.EMPLOYMENT_CONTRACT) _workLoad.value else null,
                    _contractType.value!!
                ).parseResponse(_response)
            }
        }
    }
}
