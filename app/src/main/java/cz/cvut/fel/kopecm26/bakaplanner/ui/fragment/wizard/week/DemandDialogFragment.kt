package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogDemandBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListDemandItemBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Priority
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelBottomSheetFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.util.Selection
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.DemandDialogViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DemandDialogFragment : ViewModelBottomSheetFragment<DemandDialogViewModel, DialogDemandBinding>(
    R.layout.dialog_demand,
    DemandDialogViewModel::class
) {

    private val args: DemandDialogFragmentArgs by navArgs()
    private val sharedVM: SharedViewModel by sharedViewModel()

    private val successObserver by lazy {
        Observer<Boolean> {
            if (it) {
                sharedVM.setDemandSuccess.value = Consumable(it)
                findNavController().navigateUp()
            }
        }
    }

    private val demandAdapter by lazy {
        BaseListAdapter<Selection<Priority>>(
            { layoutInflater, viewGroup, attachToRoot ->
                ListDemandItemBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    attachToRoot
                )
            },
            { item, binding, _ ->
                (binding as ListDemandItemBinding).item = item
            },
            { priority, _ ->
                args.template.id?.let { id ->
                    viewModel.updateDemand(id, priority.item)
                }
            },
            { old, new -> old.item.integerValue == new.item.integerValue },
            { old, new -> old == new }
        ).apply {
            val items = Priority.values().map { Selection(it,  args.template.priority == it.integerValue) }
            setItems(items)
        }
    }

    override fun initUi() {
        Logger.d("DemandDialog initUi ")
        binding.rvSchedules.adapter = demandAdapter

        viewModel.success.observe(this, successObserver)
    }

}
