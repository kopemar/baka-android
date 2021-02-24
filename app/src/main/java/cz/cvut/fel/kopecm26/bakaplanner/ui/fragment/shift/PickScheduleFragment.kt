package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.shift

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogPickScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListScheduleBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Schedule
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.BaseBottomSheetDialogFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.PickScheduleViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class PickScheduleFragment : BaseBottomSheetDialogFragment<PickScheduleViewModel, DialogPickScheduleBinding>(
    R.layout.dialog_pick_schedule,
    PickScheduleViewModel::class
) {

    private val sharedVM: SharedViewModel by sharedViewModel()

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
                { schedule, binding, position ->
                    (binding as ListScheduleBinding).schedule = schedule
                    binding.last = position == it.size - 1
                },
                { schedule, _ -> viewModel.addToSchedule(schedule) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    private val successObserver by lazy {
        Observer<Boolean> {
            if (it) {
                sharedVM.signUpSuccess.value = Consumable(true)
                findNavController().popBackStack(R.id.unassignedFragment, false)
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
