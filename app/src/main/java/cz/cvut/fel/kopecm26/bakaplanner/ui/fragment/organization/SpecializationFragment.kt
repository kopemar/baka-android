package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.organization

import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat.getFont
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentSpecializationBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseViewPagerAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.template.ShiftEmployeesFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchEmployeesStrategy
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.SpecializationViewModel

class SpecializationFragment: ViewModelFragment<SpecializationViewModel, FragmentSpecializationBinding>(
    R.layout.fragment_specialization,
    SpecializationViewModel::class
) {

    override val toolbar: Toolbar get() = binding.toolbar
    override var navigateUp: Boolean = true

    private val args by navArgs<SpecializationFragmentArgs>()

    private val employeeListFragment by lazy {
        ShiftEmployeesFragment(SpecializationViewModel::class, this)
    }

    private val assignObserver by lazy {
        Observer<Consumable<Boolean>> {
            it.addConsumer(CONSUMER_TAG)
            if (it.canBeConsumed(CONSUMER_TAG) && it.consume(CONSUMER_TAG)) {
                viewModel.fetchEmployees()
            }
        }
    }

    override fun initUi() {
        binding.toolbarLayout.setCollapsedTitleTypeface(getFont(requireContext(), R.font.magra))

        viewModel.setSpecializationValue(args.specialization)

        sharedViewModel.assignSuccess.observe(viewLifecycleOwner, assignObserver)

        setupViewPager()
        setupMenu()
    }

    private fun setupViewPager() {
        val fragments = listOf(employeeListFragment)
        val titles = listOf(R.string.information, R.string.schedule)

        binding.viewPager.adapter = BaseViewPagerAdapter(childFragmentManager, lifecycle, fragments)
        binding.viewPager.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, int ->
            tab.text = getString(titles[int])
        }.attach()
    }

    private fun setupMenu() {
        toolbar.inflateMenu(R.menu.specializations)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.add_employee) {
                findNavController().navigate(SpecializationFragmentDirections.navigateToSelectEmployees(FetchEmployeesStrategy.Specialization(args.specialization.id)))
            }
            true
        }
    }

    companion object {
        private const val CONSUMER_TAG = "SPECIALIZATION_FRAGMENT"
    }

}
