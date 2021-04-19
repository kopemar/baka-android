package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.profile

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentContractsBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.HeaderExpandableBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListContractBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.Headers
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.WrappedAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.ContractsViewModel

class ContractsFragment : ViewModelFragment<ContractsViewModel, FragmentContractsBinding>(
    R.layout.fragment_contracts,
    ContractsViewModel::class
) {

    override val toolbar: Toolbar
        get() = binding.contractsToolbar.toolbar

    override var navigateUp = true

    private val contractsAdapter
        get() = WrappedAdapter<Headers, Contract, Nothing>(
            { layoutInflater, viewGroup, attachToRoot ->
                HeaderExpandableBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    attachToRoot
                )
            },
            { header, binding, _ -> (binding as HeaderExpandableBinding).header = header },
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
        )

    private val observer by lazy {
        Observer<List<Contract>> {
            binding.rvContracts.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvContracts.adapter = ConcatAdapter().apply {
                if (it.any { it.active }) {
                    addAdapter(
                        contractsAdapter.apply {
                            header = Headers.ACTIVE_CONTRACTS
                            setItems(it.filter { it.active })
                        }
                    )
                }

                if (it.any { !it.active }) {
                    addAdapter(
                        contractsAdapter.apply {
                            header = Headers.INACTIVE_CONTRACTS
                            setItems(it.filter { !it.active })
                        }
                    )
                }
            }
        }
    }

    private val args by navArgs<ContractsFragmentArgs>()

    override fun initUi() {
        viewModel.setStrategy(args.type)
        viewModel.contracts.observe(this, observer)
    }
}
