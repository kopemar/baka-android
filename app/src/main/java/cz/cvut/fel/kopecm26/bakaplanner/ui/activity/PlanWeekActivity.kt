package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.appcompat.widget.Toolbar
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityWeekBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
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
    }

    companion object {
        const val SCHEDULING_PERIOD = "SCHEDULING_PERIOD"
    }
}