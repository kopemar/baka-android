package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.profile

import BaseListAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentContractsBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListContractBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ContractsViewModel

class ContractsFragment : ViewModelFragment<ContractsViewModel, FragmentContractsBinding>(
    R.layout.fragment_contracts,
    ContractsViewModel::class
) {

    private val observer by lazy {
        Observer<List<Contract>> {
            binding.rvContracts.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvContracts.adapter = BaseListAdapter<Contract>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListContractBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { contract, binding, _ -> (binding as ListContractBinding).contract = contract },
                null,
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        viewModel.contracts.observe(this, observer)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refreshContracts()
        }

        binding.toolbar.toolbar.run {
            setNavigationIcon(R.drawable.ic_mdi_back)
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

    }
}