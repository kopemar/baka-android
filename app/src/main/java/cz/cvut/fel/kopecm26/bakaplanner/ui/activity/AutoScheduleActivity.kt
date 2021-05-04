package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.lifecycle.Observer
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAutoScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AutoScheduleViewModel

class AutoScheduleActivity : ViewModelActivity<AutoScheduleViewModel, ActivityAutoScheduleBinding>(
    R.layout.activity_auto_schedule,
    AutoScheduleViewModel::class
) {

    private val errorObserver by lazy {
        Observer<Int?> {
            it?.let {
                showSnackBar(it)
                viewModel.removeError()
            }
        }
    }

    override fun initUi() {
        val period = intent?.getSerializableExtra(SCHEDULING_PERIOD) as? SchedulingPeriod
        period?.let { viewModel.setPeriod(it) }

        viewModel.errorMessage.observe(this, errorObserver)
    }

    companion object {
        const val SCHEDULING_PERIOD = "SCHEDULING_PERIOD"
    }
}
