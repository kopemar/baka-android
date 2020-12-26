package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentUnassignedBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.UnassignedViewModel

class UnassignedFragment : ViewModelFragment<UnassignedViewModel, FragmentUnassignedBinding>(
    R.layout.fragment_unassigned,
    UnassignedViewModel::class
) {

    override val viewModelOwner: ViewModelStoreOwner?
        get() = activity

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
                { findNavController().navigate(UnassignedFragmentDirections.navigateToShiftDetail(it)) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchShifts()
        }

        viewModel.shifts.observe(viewLifecycleOwner, observer)

        binding.rvShifts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Logger.d("Scroll listener ${recyclerView.computeVerticalScrollOffset()}")

                binding.mainToolbar.appBarLayout.elevation = if (recyclerView.computeVerticalScrollOffset() > 0) 6F else 0F
            }
        })
    }
}