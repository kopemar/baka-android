package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.schedule

import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseViewPagerAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.shift.ShiftInformationFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ShiftViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ShiftFragment : ViewModelFragment<ShiftViewModel, FragmentShiftBinding>(
    R.layout.fragment_shift,
    ShiftViewModel::class
) {
    override val toolbar: Toolbar get() = binding.toolbar
    override var navigateUp = true

    private val sharedVM: SharedViewModel by sharedViewModel()

    private val informationFragment by lazy {
        ShiftInformationFragment(ShiftViewModel::class, this) {
            runRemoval()
        }
    }

    private val removedObserver by lazy {
        Observer<Boolean> {
            if (it) {
                sharedVM.removeSuccess.value = Consumable(true)
                findNavController().navigateUp()
            }
        }
    }

    private val args by navArgs<ShiftFragmentArgs>()

    override fun initUi() {
        binding.toolbarLayout.setCollapsedTitleTypeface(ResourcesCompat.getFont(requireContext(), R.font.magra))
        binding.toolbarLayout.setCollapsedSubtitleTypeface(ResourcesCompat.getFont(requireContext(), R.font.magra))

        viewModel.shift.value = args.shift
        viewModel.removed.observe(this, removedObserver)

        setupMenu()
        setupViewPager()
    }

    private fun setupViewPager() {
        val fragments = listOf(informationFragment)
        val titles = listOf(R.string.information)

        binding.viewPager.adapter = BaseViewPagerAdapter(childFragmentManager, lifecycle, fragments)
        binding.viewPager.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, int ->
            tab.text = getString(titles[int])
        }.attach()
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

    private fun runRemoval() {
        showMaterialDialog(R.string.are_you_sure, title = R.string.remove_from_schedule, onPositive = { viewModel.removeFromSchedule() }, onNegative = { it.cancel() })
    }
}
