package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.schedule

import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ShiftViewModel

class ShiftFragment : ViewModelFragment<ShiftViewModel, FragmentShiftBinding>(
    R.layout.fragment_shift,
    ShiftViewModel::class
) {

    val args by navArgs<ShiftFragmentArgs>()

    override val toolbar: Toolbar
        get() = binding.toolbar.toolbar

    override var navigateUp = true

    override fun initUi() {
        viewModel.shift.value = args.shift

        initExpandable()
    }

    private fun initExpandable() {
        binding.infoHeader.run {
            binding.infoExpanded = true
            root.setOnClickListener {
                binding.infoExpanded = binding.infoExpanded?.not() ?: false
            }
        }

        binding.actionsHeader.run {
            root.setOnClickListener {
                binding.actionsExpanded = binding.actionsExpanded?.not() ?: false
            }
        }
    }
}