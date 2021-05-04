package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.scheduling

import androidx.core.text.isDigitsOnly
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogAutoScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelBottomSheetFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AutoScheduleViewModel

class AutoScheduleDialog: ViewModelBottomSheetFragment<AutoScheduleViewModel, DialogAutoScheduleBinding>(
    R.layout.dialog_auto_schedule,
    AutoScheduleViewModel::class
) {
    override val viewModelOwner: ViewModelStoreOwner?
        get() = activity

    override fun initUi() {

        binding.actvIterations.doOnTextChanged { text, _, _, _ ->
            if (text?.isDigitsOnly() == true) viewModel.setIterations(text.toString().toInt())
        }

        binding.actvNoEmpty.doOnTextChanged { text, _, _, _ ->
            if (text?.isDigitsOnly() == true) viewModel.setNoEmptyShifts(text.toString().toInt())
        }

        binding.actvDemand.doOnTextChanged { text, _, _, _ ->
            if (text?.isDigitsOnly() == true) viewModel.setDemandFulfill(text.toString().toInt())
        }

        binding.actvFreeDays.doOnTextChanged { text, _, _, _ ->
            if (text?.isDigitsOnly() == true) viewModel.setFreeDays(text.toString().toInt())
        }

        binding.actvSpecialized.doOnTextChanged { text, _, _, _ ->
            if (text?.isDigitsOnly() == true) viewModel.setSpecialized(text.toString().toInt())
        }
    }
}
