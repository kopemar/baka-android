package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.EmployeeListResponse
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils

class HomeViewModel : BaseViewModel() {
    private val _currentEmployees = MutableLiveData<EmployeeListResponse>()
    val currentEmployees: LiveData<EmployeeListResponse> = _currentEmployees

    private val _currentShift = MutableLiveData<Shift>()
    val currentShift: LiveData<Shift> = _currentShift

    private val _nextShift = MutableLiveData<Shift>()
    val nextShift: LiveData<Shift?> = _nextShift

    private val _nextWeekDays = MutableLiveData<List<Shift>>()
    val nextWeekDays: LiveData<List<Shift>> = _nextWeekDays

    private val _daysVisible = MutableLiveData(false)
    val daysVisible: LiveData<Boolean> = _daysVisible
    fun invertDaysVisible() {
        _daysVisible.value = _daysVisible.value?.not()
    }

    private val _employeesVisible = MutableLiveData(false)
    val employeesVisible: LiveData<Boolean> = _employeesVisible
    fun invertEmployeesVisible() {
        _employeesVisible.value = _employeesVisible.value?.not()
    }

    private val _periodDetailVisible = MutableLiveData(false)
    val periodDetailVisible: LiveData<Boolean> = _periodDetailVisible
    fun invertPeriodDetailVisible() {
        _periodDetailVisible.value = _periodDetailVisible.value?.not()
    }

    private val _upcomingPeriod = MutableLiveData<SchedulingPeriod>()
    val upcomingPeriod: LiveData<SchedulingPeriod> = _upcomingPeriod

    init {
        getAll()
    }

    fun refresh() {
        working.work {
            shiftRepository.refreshAllShifts().let(::handleRefreshResponse)
            getAll()
        }
    }

    private fun getAll() {
        working.work {
            if (PrefsUtils.getUser()?.manager != true) {
                getShifts()
                getCurrentShift()
                getNextShift()
                getNextWeekShifts()
            } else {
                getCurrentEmployees()
                getUpcomingPeriod()
            }
        }
    }

    private fun handleRefreshResponse(response: ResponseModel<List<Shift>>) {
        if (response is ResponseModel.ERROR<*>) {
            response.errorType?.let(::parseError)
        } else {
            getAll()
        }
    }

    private suspend fun getCurrentEmployees() {
        userRepository.getOrganizationEmployees(true).parseResponse(_currentEmployees)
    }

    private suspend fun getUpcomingPeriod() {
        planningRepository.getUpcomingPeriod().parseResponse(_upcomingPeriod)
    }

    private suspend fun getNextWeekShifts() {
        Logger.d(shiftRepository.getNextWeekShifts())
        _nextWeekDays.value = shiftRepository.getNextWeekShifts()
    }

    private suspend fun getCurrentShift() {
        Logger.d(shiftRepository.getCurrentShift())
        _currentShift.value = shiftRepository.getCurrentShift()
    }

    private suspend fun getNextShift() {
        Logger.d(shiftRepository.getNextShift())
        _nextShift.value = shiftRepository.getNextShift()
    }

    private suspend fun getShifts() {
        shiftRepository.getAllShifts()
    }
}
