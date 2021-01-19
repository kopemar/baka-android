package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.schedule

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ShiftViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.nav.ScheduleNavViewModel

class ShiftFragment : ViewModelFragment<ShiftViewModel, FragmentShiftBinding>(
    R.layout.fragment_shift,
    ShiftViewModel::class
) {
    override val toolbar: Toolbar get() = binding.sToolbar.toolbar
    override var navigateUp = true

    private val navVM by navGraphViewModels<ScheduleNavViewModel>(R.id.schedule)

    private val removedObserver by lazy {
        Observer<Boolean> {
            if (it) {
                navVM.success.value = it
                findNavController().navigateUp()
            }
        }
    }

    private val args by navArgs<ShiftFragmentArgs>()

    override fun initUi() {
        viewModel.shift.value = args.shift
        viewModel.removed.observe(this, removedObserver)
        initExpandable()
        setupMenu()
//        binding.btnRemove.setOnClickListener { runRemoval() }
    }

    private fun setupMenu() {
        if (PrefsUtils.getUser()?.agreement == true && viewModel.shift.value?.canBeRemoved == true) {
            toolbar.inflateMenu(R.menu.remove)
        }

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_remove) {
                runRemoval()
            }
            true
        }
    }

    private fun initExpandable() {
//        binding.infoHeader.run {
//            binding.infoExpanded = false
//            root.setOnClickListener {
//                binding.infoExpanded = binding.infoExpanded?.not() ?: false
//            }
//        }
    }

    private fun runRemoval() {
        showMaterialDialog(R.string.are_you_sure, title = R.string.remove_from_schedule,  onPositive = { viewModel.removeFromSchedule() }, onNegative = { it.cancel() })
    }
}