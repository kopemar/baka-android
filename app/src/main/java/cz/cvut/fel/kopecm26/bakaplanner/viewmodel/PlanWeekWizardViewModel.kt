package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.PlanWeekWizard
import cz.cvut.fel.kopecm26.bakaplanner.util.TwoWayListIterator

class PlanWeekWizardViewModel : BaseViewModel() {
    private val steps = PlanWeekWizard.getSteps()

    /**
     * Number of steps in wizard
     */
    val stepsCount = steps.size

    private val _finished = MutableLiveData(false)
    val finished: LiveData<Boolean> = _finished

    private val _lastStep = MutableLiveData(false)
    val lastStep: LiveData<Boolean> = _lastStep

    val stepsIterator = TwoWayListIterator(steps)

    private val _step = MutableLiveData(stepsIterator.next())

    /**
     * Current step in wizard
     */
    val step: LiveData<PlanWeekWizard> = _step

    fun nextStep() {
        if (stepsIterator.hasNext()) _step.value = stepsIterator.next()
        else _lastStep.value = true
    }

    fun prevStep() {
        if (stepsIterator.hasPrevious()) _step.value = stepsIterator.previous()
    }

    fun submitShiftTemplates(
        periodId: Int,
        startTime: String,
        endTime: String,
        shiftHours: Int,
        breakMinutes: Int,
        perDay: Int,
        excluded: Map<Int, ArrayList<Int>>,
        workingDays: List<Int>
    ) {
        working.work {
            planningRepository.createShiftTemplates(
                periodId,
                startTime,
                endTime,
                shiftHours,
                breakMinutes,
                perDay,
                excluded,
                workingDays
            ).let(::handlePlanningResult)
        }
    }

    private fun handlePlanningResult(response: ResponseModel<List<ShiftTemplate>>) {
        if (response is ResponseModel.SUCCESS) {
            _finished.value = true
        } else if (response is ResponseModel.ERROR) {
            response.errorType?.let(::parseError)
        }
    }
}