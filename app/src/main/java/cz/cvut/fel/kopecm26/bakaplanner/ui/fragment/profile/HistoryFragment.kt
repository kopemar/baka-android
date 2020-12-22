package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.profile

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentHistoryBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.HistoryViewModel

class HistoryFragment : ViewModelFragment<HistoryViewModel, FragmentHistoryBinding>(
    R.layout.fragment_history,
    HistoryViewModel::class
) {

    override val toolbar: Toolbar
        get() = binding.tToolbar.toolbar

    override var navigateUp: Boolean = true

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
                { findNavController().navigate(HistoryFragmentDirections.navigateToShiftDetail(it.id)) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        super.initUi()

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshShifts()
        }

        viewModel.shifts.observe(viewLifecycleOwner, observer)
    }
}