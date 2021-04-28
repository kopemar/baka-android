package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.template

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftTemplateBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseViewPagerAdapter
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

    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    private val args by navArgs<ShiftTemplateFragmentArgs>()

    private val employeesFragment by lazy {
        ShiftEmployeesFragment(ShiftTemplateViewModel::class).apply {
            itemLongPressListener = {
                findNavController().navigate(ShiftTemplateFragmentDirections.showEmployeeBottomSheet(it))
            }
        }
    }

    private val shiftInfoFragment by lazy { ShiftInformationFragment() }

    override fun initUi() {
        viewModel.template.postValue(args.template)

        binding.btnAdd.setOnClickListener { navigateToSignUpFragment() }

        setupMenu()
        setupViewPager()
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

    private fun setupViewPager() {

        val fragments = if (PrefsUtils.getUser()?.manager == true) listOf(shiftInfoFragment, employeesFragment) else listOf(shiftInfoFragment)
        val titles = listOf(R.string.information, R.string.employees)

        binding.viewPager.adapter = BaseViewPagerAdapter(childFragmentManager, lifecycle, fragments)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, int ->
            tab.text = getString(titles[int])
        }.attach()
    }

    private fun navigateToSignUpFragment() = viewModel.template.value?.let {
        findNavController().navigate(
            ShiftTemplateFragmentDirections.showDialog(it)
        )
    }
}
