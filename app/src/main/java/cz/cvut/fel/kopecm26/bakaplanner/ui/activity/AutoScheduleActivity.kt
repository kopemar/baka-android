package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAutoScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.finishWithOkResult
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AutoScheduleViewModel

class AutoScheduleActivity : ViewModelActivity<AutoScheduleViewModel, ActivityAutoScheduleBinding>(
    R.layout.activity_auto_schedule,
    AutoScheduleViewModel::class
) {
    override val toolbar: Toolbar get() = binding.toolbar.toolbar
    override val navigateUp = true
    override val navigateUpRes: Int get() = R.drawable.ic_mdi_close
    override val onNavigateUp: (BackNavigation) -> Unit = {
        if (viewModel.working.value == false) finishWithOkResult()
    }

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

        toolbar.title = getString(R.string.auto_schedule)

        binding.btnSchedule.setOnClickListener {
            if (viewModel.success.value == true) {
                Logger.d("Test")
                finishWithOkResult()
            } else {
                viewModel.callAutoSchedule()
            }
        }

        viewModel.errorMessage.observe(this, errorObserver)
    }

    companion object {
        const val SCHEDULING_PERIOD = "SCHEDULING_PERIOD"
    }
}
