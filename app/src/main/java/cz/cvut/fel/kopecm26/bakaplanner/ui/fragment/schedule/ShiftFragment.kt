package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.schedule

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ShiftViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.RemoveShiftViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ShiftFragment : ViewModelFragment<ShiftViewModel, FragmentShiftBinding>(
    R.layout.fragment_shift,
    ShiftViewModel::class
) {
    override val viewModelOwner: ViewModelStoreOwner? get() = activity
    override val toolbar: Toolbar get() = binding.sToolbar.toolbar
    override var navigateUp = true

    private val removeVM: RemoveShiftViewModel by sharedViewModel()

    private val removedObserver by lazy {
        Observer<Boolean> {
            if (it) {
                removeVM.success.value = it
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
        binding.btnRemove.setOnClickListener { viewModel.removeFromSchedule() }
        binding.btnSignUp.setOnClickListener { navigateToSignUpFragment() }
    }

    private fun setupMenu() {
        if (viewModel.shift.value?.schedule_id == null) {
            toolbar.inflateMenu(R.menu.sign_up)
        }

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_sign_up) { navigateToSignUpFragment() }
            true
        }
    }

    private fun navigateToSignUpFragment() = viewModel.shift.value?.let {
        findNavController().navigate(ShiftFragmentDirections.navigateToPickSchedule(it))
    }

    private fun initExpandable() {
        binding.infoHeader.run {
            binding.infoExpanded = false
            root.setOnClickListener {
                binding.infoExpanded = binding.infoExpanded?.not() ?: false
            }
        }
    }
}