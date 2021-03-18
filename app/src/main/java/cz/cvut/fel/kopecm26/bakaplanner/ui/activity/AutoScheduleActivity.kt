package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAutoScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingPeriod
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AutoScheduleViewModel
import kotlinx.coroutines.flow.collect

class AutoScheduleActivity : ViewModelActivity<AutoScheduleViewModel, ActivityAutoScheduleBinding>(
    R.layout.activity_auto_schedule,
    AutoScheduleViewModel::class
) {
    override val toolbar: Toolbar get() = binding.toolbar.toolbar
    override val navigateUp = true
    override val navigateUpRes: Int get() = R.drawable.ic_mdi_close

    override fun initUi() {
        val period = intent?.getSerializableExtra(SCHEDULING_PERIOD) as? SchedulingPeriod
        period?.let { viewModel.setPeriod(it) }

        lifecycleScope.launchWhenStarted {
            viewModel.working.observe(this@AutoScheduleActivity) {
                binding.ivIcon.setMinAndMaxFrame(0, 30)
                if (it) binding.ivIcon.playAnimation()
                else binding.ivIcon.setMinAndMaxFrame(0, 60)
            }
            viewModel.scheduleState.collect { onScheduleState(it) }
        }
    }

    private fun onScheduleState(state: ResponseModel<Boolean>?) {
        if (state is ResponseModel.SUCCESS) {
            showSnackBar("Hey success")
        } else if (state is ResponseModel.ERROR) {
            showSnackBar(state.errorType?.messageRes ?: R.string.unknown_error)
        }
    }

    companion object {
        const val SCHEDULING_PERIOD = "SCHEDULING_PERIOD"
    }
}
