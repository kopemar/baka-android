package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityWeekBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week.SelectWorkingDaysFragmentDirections
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.PlanWeekWizard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PeriodDaysViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PlanWeekWizardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanWeekActivity: ViewModelActivity<PlanWeekWizardViewModel, ActivityWeekBinding>(R.layout.activity_week, PlanWeekWizardViewModel::class) {

    override val toolbar: Toolbar get() = binding.toolbar.toolbar
    override val navigateUp = true
    override val navigateUpRes: Int get() = R.drawable.ic_mdi_close

    private val periodDaysViewModel: PeriodDaysViewModel by viewModel()
    private val period get() = intent?.extras?.getSerializable(SCHEDULING_PERIOD) as? SchedulingPeriod

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun initUi() {
        periodDaysViewModel.setPeriod(period)
        Logger.d(period)

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
                PlanWeekWizard.PLAN_DAYS -> {}
                PlanWeekWizard.ADJUST_SHIFTS -> {}
                PlanWeekWizard.REVIEW -> {}
                else -> {}
            }

        }
    }



    companion object {
        const val SCHEDULING_PERIOD = "SCHEDULING_PERIOD"
    }
}