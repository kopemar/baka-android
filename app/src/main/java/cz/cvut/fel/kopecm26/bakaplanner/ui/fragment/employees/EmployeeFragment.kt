package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.employees

import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseViewPagerAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchSpecializationStrategy
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.EmployeeViewModel

class EmployeeFragment : ViewModelFragment<EmployeeViewModel, FragmentEmployeeBinding>(
    R.layout.fragment_employee,
    EmployeeViewModel::class
) {
    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    override val toolbar: Toolbar get() = binding.toolbar
    override var navigateUp: Boolean = true

    private val employeeInfoFragment by lazy {
        EmployeeInformationFragment(onSpecializations)
    }

    private val onSpecializations = {
        findNavController().navigate(
            EmployeeFragmentDirections.navigateToSpecializations(
                FetchSpecializationStrategy.Employee(args.employee.id)
            )
        )
    }

    private val employeeShiftsFragment by lazy { EmployeeShiftsFragment() }

    private val args by navArgs<EmployeeFragmentArgs>()

    override fun initUi() {
        binding.toolbarLayout.setCollapsedTitleTypeface(getFont(requireContext(), R.font.magra))

        viewModel.setEmployeeValue(args.employee)
        setupViewPager()
    }

    private fun setupViewPager() {
        val fragments = listOf(employeeInfoFragment, employeeShiftsFragment)
        val titles = listOf(R.string.information, R.string.schedule)

        binding.viewPager.adapter = BaseViewPagerAdapter(childFragmentManager, lifecycle, fragments)
        binding.viewPager.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, int ->
            tab.text = getString(titles[int])
        }.attach()
    }

}
