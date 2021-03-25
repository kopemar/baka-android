package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.wizard.week

import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.DialogSpecializatedDemandBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListSpecializationBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelBottomSheetFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.SpecializedDemandViewModel

class SpecializedDemandDialog :
    ViewModelBottomSheetFragment<SpecializedDemandViewModel, DialogSpecializatedDemandBinding>(
        R.layout.dialog_specializated_demand,
        SpecializedDemandViewModel::class
    ) {

    private val args: SpecializedDemandDialogArgs by navArgs()

    private val observer by lazy {
        Observer<List<Specialization>> {
            binding.rvShiftTimes.adapter = BaseListAdapter<Specialization>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListSpecializationBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { specialization, binding, _ ->
                    (binding as ListSpecializationBinding).specialization = specialization
                },
                { specialization, _ -> viewModel.createSpecializedShift(specialization) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        viewModel.setTemplate(args.template)

        viewModel.specializations.observe(this, observer)
    }
}