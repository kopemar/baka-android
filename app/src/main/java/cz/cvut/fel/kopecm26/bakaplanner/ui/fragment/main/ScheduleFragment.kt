package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.vvalidator.util.hide
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ScheduleViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.nav.ScheduleNavViewModel

class ScheduleFragment : ViewModelFragment<ScheduleViewModel, FragmentScheduleBinding>(
    R.layout.fragment_schedule,
    ScheduleViewModel::class
) {

    private val navVM by navGraphViewModels<ScheduleNavViewModel>(R.id.schedule)

    private val removeObserver by lazy {
        Observer<Boolean?> {
            if (it == true) {
                navVM.success.value = null
                viewModel.refreshShifts()

                showSnackBar(R.string.successfully_removed).apply {
                    this.setAction(R.string.ok) { snack -> snack.hide() }
                }
            }
        }
    }

    private val observer by lazy {
        Observer<List<Shift>> {
            binding.rvShifts.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvShifts.adapter = BaseListAdapter<Shift>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListShiftBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { shift, binding, _ -> (binding as ListShiftBinding).shift = shift },
                {
                    findNavController().navigate(ScheduleFragmentDirections.navigateToShiftDetail(it))
                },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        initObservers()
    }

    private fun initObservers() {
        viewModel.shifts.observe(viewLifecycleOwner, observer)

        binding.rvShifts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Logger.d("Scroll listener ${recyclerView.computeVerticalScrollOffset()}")

                binding.mainToolbar.appBarLayout.elevation =
                    if (recyclerView.computeVerticalScrollOffset() > 0) 6F else 0F
            }
        })

//        navVM.success.observe(this, removeObserver)
    }
}
