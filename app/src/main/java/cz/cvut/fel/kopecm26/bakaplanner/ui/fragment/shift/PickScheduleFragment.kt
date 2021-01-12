package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.shift

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentPickScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Schedule
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PickScheduleViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SignUpToShiftViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PickScheduleFragment : ViewModelFragment<PickScheduleViewModel, FragmentPickScheduleBinding>(
    R.layout.fragment_pick_schedule,
    PickScheduleViewModel::class
) {

    private val sharedVM: SignUpToShiftViewModel by sharedViewModel()

    override val toolbar: Toolbar get() = binding.pToolbar.toolbar

    override var navigateUp: Boolean = true

    private val args by navArgs<PickScheduleFragmentArgs>()

    val observer by lazy {
        Observer<List<Schedule>> {
            binding.rvSchedules.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvSchedules.adapter = BaseListAdapter<Schedule>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListScheduleBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { schedule, binding, _ -> (binding as ListScheduleBinding).schedule = schedule },
                { viewModel.addToSchedule(it) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    private val successObserver by lazy {
        Observer<Boolean> {
            if (it) {
                sharedVM.success.value = true
                findNavController().popBackStack(R.id.menu_shifts, false)
            }
        }
    }

    override fun initUi() {
        viewModel.template.value = args.template
        viewModel.schedules.observe(viewLifecycleOwner, observer)
        viewModel.success.observe(this, successObserver)
        viewModel.template.value?.id?.let { viewModel.fetchSchedules(it) }
    }
}