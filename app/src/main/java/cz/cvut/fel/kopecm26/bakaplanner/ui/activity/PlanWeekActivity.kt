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
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.PlanDaysFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.ReviewFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.SelectWorkingDaysFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.PlanWeekWizard
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.finishWithOkResult
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
        setMenuVisibility()

        planDaysViewModel.shiftTimeCalculations.observe(this, shiftCalcObserver)
        viewModel.finished.observe(this, finishObserver)

        if (mode == MODE_UPDATE_DEMAND) {
            binding.currentStepText.isVisible = false
            viewModel.forceFinish()

            val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
            navHostFragment.navController.navigate(R.id.planDemandFragment)
        }

        toolbar.setTitle(R.string.plan_week)

        setupBackButton()
        setupNextButton()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            val nav = findNavController(R.id.nav_host_fragment)
            if (viewModel.step.value != PlanWeekWizard.SELECT_DAYS) {
                prevStep()
                nav.navigateUp()
            }
        }
    }

    // TODO simplify this
    private fun setupNextButton() {
        binding.btnNext.setOnClickListener {
            val nav = findNavController(R.id.nav_host_fragment)
            when (viewModel.step.value) {
                PlanWeekWizard.SELECT_DAYS -> {
                    nav.navigate(SelectWorkingDaysFragmentDirections.navigateToPlanDays())
                }
                PlanWeekWizard.PLAN_DAYS -> {
                    fetchCalculations()
                    nav.navigate(PlanDaysFragmentDirections.navigateToReview())
                }
                PlanWeekWizard.REVIEW -> {
                    nav.navigate(ReviewFragmentDirections.navigateToAdjustShifts())
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
            viewModel.nextStep()
            setMenuVisibility()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.check_time, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        if (viewModel.finished.value != true) {
            prevStep()
            super.onBackPressed()
        } else {
            finishWithOkResult()
        }
    }

    private fun setMenuVisibility() {
        menu?.findItem(R.id.action_check_time)?.isVisible =
            viewModel.step.value == PlanWeekWizard.PLAN_DAYS
    }

    private fun fetchCalculations() {
        periodDaysViewModel.period.value?.id?.let {
            planDaysViewModel.fetchShiftTimeCalculations(it)
        }
    }

    private fun prevStep() {
        viewModel.prevStep()
        setMenuVisibility()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_check_time) {
            fetchCalculations()
            findNavController(R.id.nav_host_fragment).navigate(PlanDaysFragmentDirections.showShiftTimesDialog())
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val MODE = "MODE"
        const val MODE_UPDATE_DEMAND = 4242
        const val SCHEDULING_PERIOD = "SCHEDULING_PERIOD"
    }
}
