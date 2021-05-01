package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.profile

import android.app.Activity
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentContractsBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.HeaderExpandableBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListContractBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Contract
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.AddContractActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.Headers
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.WrappedAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.FetchContractsStrategy
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
                ListContractBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    attachToRoot
                )
            },
            { contract, binding, _ -> (binding as ListContractBinding).contract = contract },
            { item, _ ->
                Logger.d("${item}")
                ContractsFragmentDirections.showContractDialog(item) },
            { old, new -> old.id == new.id },
            { old, new -> old == new },
            { layoutInflater, viewGroup, attachToRoot ->
                HeaderExpandableBinding.inflate(
                    layoutInflater,
                    viewGroup,
                    attachToRoot
                )
            },
            { header, binding, _ -> (binding as HeaderExpandableBinding).header = header },
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
        val type = args.type
        if (type is FetchContractsStrategy.Employee) {
            binding.fab.setOnClickListener {
                startActivityForResult<AddContractActivity>(ADD_CONTRACT_REQUEST) {
                    apply { putInt(AddContractActivity.EMPLOYEE_EXTRA_KEY, type.id) }
                }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_CONTRACT_REQUEST && resultCode == Activity.RESULT_OK) {
            viewModel.refreshContracts()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val ADD_CONTRACT_REQUEST = 4904
    }
}
