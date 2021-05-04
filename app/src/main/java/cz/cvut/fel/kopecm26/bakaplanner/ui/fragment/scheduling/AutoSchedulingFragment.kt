package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.scheduling

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentAutoScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.AutoScheduleActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.finishWithOkResult
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AutoScheduleViewModel

/**
 * This fragment is tightly coupled with [AutoScheduleActivity], not meant to be reused
 */
class AutoSchedulingFragment: ViewModelFragment<AutoScheduleViewModel, FragmentAutoScheduleBinding>(
    R.layout.fragment_auto_schedule,
    AutoScheduleViewModel::class
) {

    override val viewModelOwner: ViewModelStoreOwner?
        get() = activity
    override val toolbar: Toolbar get() = binding.toolbar.toolbar
    override var navigateUp = true
    override val navigateUpRes: Int get() = R.drawable.ic_mdi_close
    override val onNavigateUp: () -> Unit = {
        if (viewModel.working.value == false) (activity as AutoScheduleActivity).finishWithOkResult()
    }

    override fun initUi() {
        toolbar.title = getString(R.string.auto_schedule)

        binding.btnAdjust.setOnClickListener {
            findNavController().navigate(AutoSchedulingFragmentDirections.showScheduleDialog())
        }

        binding.btnSchedule.setOnClickListener {
            if (viewModel.success.value == true) {
                (activity as AutoScheduleActivity).finishWithOkResult()
            } else {
                viewModel.callAutoSchedule()
            }
        }
    }
}