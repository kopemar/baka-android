package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.profile

import androidx.navigation.fragment.navArgs
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogContractBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListWorkedHoursBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.YearlyHours
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelBottomSheetFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ContractsViewModel

class ContractDialogFragment :
    ViewModelBottomSheetFragment<ContractsViewModel, DialogContractBinding>(
        R.layout.dialog_contract,
        ContractsViewModel::class
    ) {

    private val args: ContractDialogFragmentArgs by navArgs()

    override fun initUi() {
        binding.contract = args.contract
        Logger.d("init")
        val hours = args.contract.hours
        if (hours != null) {
            binding.rvDates.adapter = BaseListAdapter<YearlyHours>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListWorkedHoursBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { item, binding, _ -> (binding as ListWorkedHoursBinding).hours = item },
                null,
                { old, new -> old.year == new.year },
                { old, new -> old == new }
            ).apply {
                setItems(hours)
            }
        }

        super.initUi()
    }
}
