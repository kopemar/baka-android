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
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchEmployeesStrategy
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ShiftTemplateViewModel

class ShiftTemplateFragment :
    ViewModelFragment<ShiftTemplateViewModel, FragmentShiftTemplateBinding>(
        R.layout.fragment_shift_template,
        ShiftTemplateViewModel::class
    ) {
    override val toolbar: Toolbar get() = binding.toolbar
    override var navigateUp = true

    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    private val args by navArgs<ShiftTemplateFragmentArgs>()

    private val employeesFragment by lazy {
        ShiftEmployeesFragment(ShiftTemplateViewModel::class).apply {
            itemLongPressListener = {
                findNavController().navigate(
                    ShiftTemplateFragmentDirections.showEmployeeBottomSheet(
                        it
                    )
                )
            }
        }
    }

    private val shiftInfoFragment by lazy {
        ShiftInformationFragment {
            navigateToSignUpFragment()
        }
    }

    override fun initUi() {
        viewModel.template.postValue(args.template)

        setupMenu()
        setupViewPager()
    }

    private fun setupMenu() {
        if (PrefsUtils.getUser()?.agreement == true) {
            toolbar.inflateMenu(R.menu.sign_up)
        } else if (PrefsUtils.getUser()?.manager == true && !args.period.submitted) {
            toolbar.inflateMenu(R.menu.add_employees)
        }

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_sign_up) {
                navigateToSignUpFragment()
            } else if (it.itemId == R.id.add_employee) {
                findNavController().navigate(
                    ShiftTemplateFragmentDirections.navigateToSelectEmployees(
                        FetchEmployeesStrategy.Template(args.template.id ?: 0)
                    )
                )
            }
            true
        }
    }

    private fun setupViewPager() {

        val fragments = if (PrefsUtils.getUser()?.manager == true) listOf(
            shiftInfoFragment,
            employeesFragment
        ) else listOf(shiftInfoFragment)
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
