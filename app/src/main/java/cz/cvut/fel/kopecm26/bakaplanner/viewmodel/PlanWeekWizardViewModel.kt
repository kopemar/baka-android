package cz.cvut.fel.kopecm26.bakaplanner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    val stepsIterator = TwoWayListIterator(steps)

    private val _step = MutableLiveData(stepsIterator.next())

    /**
     * Current step in wizard
     */
    val step: LiveData<PlanWeekWizard> = _step

    fun nextStep() {
        if (stepsIterator.hasNext()) _step.value = stepsIterator.next()
        else _finished.value = true
    }

    fun prevStep() {
        if (stepsIterator.hasPrevious()) _step.value = stepsIterator.previous()
    }
}