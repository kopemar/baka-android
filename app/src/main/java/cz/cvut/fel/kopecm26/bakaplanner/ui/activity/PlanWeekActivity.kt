package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityWeekBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.PlanDaysFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.ReviewFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.SelectWorkingDaysFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.PlanWeekWizard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanWeekWizardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanWeekActivity: ViewModelActivity<PlanWeekWizardViewModel, ActivityWeekBinding>(R.layout.activity_week, PlanWeekWizardViewModel::class) {

    override val toolbar: Toolbar get() = binding.toolbar.toolbar
    override val navigateUp = true
    override val navigateUpRes: Int get() = R.drawable.ic_mdi_close

    private val periodDaysViewModel: PeriodDaysViewModel by viewModel()
    private val planDaysViewModel: PlanDaysViewModel by viewModel()

    private val period get() = intent?.extras?.getSerializable(SCHEDULING_PERIOD) as? SchedulingPeriod

    private var menu: Menu? = null

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun initUi() {
        periodDaysViewModel.setPeriod(period)
        setMenuVisibility()

        toolbar.setTitle(R.string.plan_week)

        setupBackButton()
        setupNextButton()
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            val nav = findNavController(R.id.nav_host_fragment)
            if (viewModel.step.value != PlanWeekWizard.SELECT_DAYS) {
                viewModel.prevStep()
                nav.navigateUp()
            }
            setMenuVisibility()
        }
    }

    private fun setupNextButton() {
        binding.btnNext.setOnClickListener {
            val nav = findNavController(R.id.nav_host_fragment)
            when (viewModel.step.value) {
                PlanWeekWizard.SELECT_DAYS -> {
                    viewModel.nextStep()
                    nav.navigate(SelectWorkingDaysFragmentDirections.navigateToPlanDays())
                }
                PlanWeekWizard.PLAN_DAYS -> {
                    viewModel.nextStep()
                    fetchCalculations()
                    nav.navigate(PlanDaysFragmentDirections.navigateToReview())
                }
                PlanWeekWizard.REVIEW -> {
                    viewModel.nextStep()
                    nav.navigate(ReviewFragmentDirections.navigateToAdjustShifts())
                }
                PlanWeekWizard.ADJUST_SHIFTS -> { }
                else -> {}
            }
            setMenuVisibility()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        menuInflater.inflate(R.menu.check_time, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        viewModel.prevStep()
        super.onBackPressed()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_check_time) {
            fetchCalculations()
            findNavController(R.id.nav_host_fragment).navigate(PlanDaysFragmentDirections.showShiftTimesDialog())
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val SCHEDULING_PERIOD = "SCHEDULING_PERIOD"
    }
}