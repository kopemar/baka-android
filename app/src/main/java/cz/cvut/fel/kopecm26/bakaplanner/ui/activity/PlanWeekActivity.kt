package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityWeekBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTimeCalculation
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.AdjustShiftsFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.PlanDaysFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.PlanDaysFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.ReviewFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.SelectWorkingDaysFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.PlanWeekWizard
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.getFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.ifNotNull
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AdjustShiftsViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanWeekWizardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanWeekActivity : ViewModelActivity<PlanWeekWizardViewModel, ActivityWeekBinding>(
    R.layout.activity_week,
    PlanWeekWizardViewModel::class
) {

    override val toolbar: Toolbar get() = binding.toolbar.toolbar
    override val navigateUp = true
    override val navigateUpRes: Int get() = R.drawable.ic_mdi_close
    override val onNavigateUp: ((BackNavigation) -> Unit) get() = {
        if (it == BackNavigation.SUPPORT || viewModel.firstStep.value == true) {
            if (viewModel.working.value == false) {
                showMaterialDialog(
                    R.string.are_you_sure_you_want_to_cancel,
                    positive = R.string.yes,
                    onPositive = {
                        finish()
                    },
                    negative = R.string.no,
                    onNegative = { dialog ->
                        dialog.dismiss()
                    }
                )
            }
        } else {
            val nav = findNavController(binding.navHostFragment.id)
            nav.navigateUp()
            prevStep()
        }
    }

    private val periodDaysViewModel: PeriodDaysViewModel by viewModel()
    private val planDaysViewModel: PlanDaysViewModel by viewModel()
    private val adjustViewModel: AdjustShiftsViewModel by viewModel()

    private val shiftCalcObserver by lazy {
        Observer<List<ShiftTimeCalculation>> { calculations ->
            periodDaysViewModel.periodDays.value?.let { days ->
                adjustViewModel.mapDays(
                    days,
                    calculations
                )
            }
        }
    }

    private val finishObserver by lazy {
        Observer<Boolean> {
            if (it && viewModel.step.value == PlanWeekWizard.ADJUST_SHIFTS) {
                findNavController(binding.navHostFragment.id).navigate(
                    AdjustShiftsFragmentDirections.navigateToPlanWeekFinished()
                )
            }
        }
    }

    private val period by lazy { intent?.extras?.getSerializable(SCHEDULING_PERIOD) as? SchedulingPeriod }

    private val mode by lazy { intent?.extras?.getInt(MODE) }

    private var menu: Menu? = null

    override fun initUi() {
        periodDaysViewModel.setPeriod(period)

        planDaysViewModel.shiftTimeCalculations.observe(this, shiftCalcObserver)
        viewModel.finished.observe(this, finishObserver)

        if (mode == MODE_UPDATE_DEMAND) {
            binding.currentStepText.isVisible = false
            viewModel.forceFinish()

            val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
            navHostFragment.navController.navigate(R.id.planDemandFragment)
        }

        toolbar.setTitle(R.string.plan_week)
    }

    private fun setupMenu() {
        val nav = findNavController(R.id.nav_host_fragment)

        when (viewModel.step.value) {
            PlanWeekWizard.SELECT_DAYS -> {
                nav.navigate(SelectWorkingDaysFragmentDirections.navigateToPlanDays())
                viewModel.nextStep()
            }
            PlanWeekWizard.PLAN_DAYS -> {
                validateForm()
            }
            PlanWeekWizard.REVIEW -> {
                nav.navigate(ReviewFragmentDirections.navigateToAdjustShifts())
                viewModel.nextStep()
            }
            PlanWeekWizard.ADJUST_SHIFTS -> {
                ifNotNull(
                    periodDaysViewModel.period.value,
                    planDaysViewModel.startTime.value,
                    planDaysViewModel.endTime.value,
                    planDaysViewModel.breakMinutes.value,
                    planDaysViewModel.shiftHours.value,
                    planDaysViewModel.shiftsPerDay.value
                ) {
                    Logger.d(adjustViewModel.mapExcludedToMap())
                    viewModel.submitShiftTemplates(
                        periodDaysViewModel.period.value!!.id,
                        planDaysViewModel.startTime.value!!,
                        planDaysViewModel.endTime.value!!,
                        planDaysViewModel.shiftHours.value!!,
                        planDaysViewModel.breakMinutes.value!!,
                        planDaysViewModel.shiftsPerDay.value!!,
                        adjustViewModel.mapExcludedToMap(),
                        periodDaysViewModel.mapWorkingDayIdsToList()
                    )
                }
            }
        }
        // Form is validated in PlanWeekWizard.PLAN_DAYS, can go to next otherwise
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.check_time, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun fetchCalculations() {
        periodDaysViewModel.period.value?.id?.let {
            planDaysViewModel.fetchShiftTimeCalculations(it)
        }
    }

    private fun prevStep() {
        viewModel.prevStep()
    }

    private fun validateForm() {
        val fragment = getFragment<PlanDaysFragment>(R.id.nav_host_fragment)
        Logger.d("fragment: ${supportFragmentManager.findFragmentById(R.id.nav_host_fragment)?.childFragmentManager?.fragments} / $fragment")
        fragment?.validateForm {
            fetchCalculations()
            if (it.success()) {
                val nav = findNavController(binding.navHostFragment.id)
                nav.navigate(PlanDaysFragmentDirections.navigateToReview())
                viewModel.nextStep()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_next) {
            setupMenu()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val MODE = "MODE"
        const val MODE_UPDATE_DEMAND = 4242
        const val SCHEDULING_PERIOD = "SCHEDULING_PERIOD"
    }
}
