package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.unit

import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftTemplateBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ShiftTemplateViewModel

class ShiftTemplateFragment :
    ViewModelFragment<ShiftTemplateViewModel, FragmentShiftTemplateBinding>(
        R.layout.fragment_shift_template,
        ShiftTemplateViewModel::class
    ) {
    override val toolbar: Toolbar get() = binding.sToolbar.toolbar
    override var navigateUp = true

    private val args by navArgs<ShiftTemplateFragmentArgs>()

    override fun initUi() {
        viewModel.template.value = args.template

        binding.btnAdd.setOnClickListener { navigateToSignUpFragment() }

        setupMenu()
    }

    private fun setupMenu() {
        if (PrefsUtils.getUser()?.agreement == true) {
            toolbar.inflateMenu(R.menu.sign_up)
        }

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_sign_up) {
                navigateToSignUpFragment()
            }
            true
        }
    }

    private fun navigateToSignUpFragment() = viewModel.template.value?.let {
        findNavController().navigate(
            ShiftTemplateFragmentDirections.showDialog(it)
        )
    }
}
