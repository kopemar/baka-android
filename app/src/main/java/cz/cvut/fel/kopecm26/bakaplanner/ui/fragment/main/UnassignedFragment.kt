package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.vvalidator.util.hide
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentUnassignedBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListTemplatesBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.Consumable
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.UnassignedViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UnassignedFragment : ViewModelFragment<UnassignedViewModel, FragmentUnassignedBinding>(
    R.layout.fragment_unassigned,
    UnassignedViewModel::class
) {

    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    private val sharedVM: SharedViewModel by sharedViewModel()

    private val successObserver by lazy {
        Observer<Consumable<Boolean>> {
            it.addConsumer(CONSUMER_TAG)
            if (it.canBeConsumed(CONSUMER_TAG) && it.consume(CONSUMER_TAG)) {
                viewModel.fetchShifts()
                showSnackBar(R.string.successfully_signed_up).apply {
                    this.setAction(R.string.ok) { snack -> snack.hide() }
                }
            }
        }
    }

    private val removeObserver by lazy {
        Observer<Consumable<Boolean>> {
            it.addConsumer(CONSUMER_TAG)
            if (it.canBeConsumed(CONSUMER_TAG) && it.consume(CONSUMER_TAG)) {
                viewModel.fetchShifts()
            }
        }
    }

    private val observer by lazy {
        Observer<List<ShiftTemplate>> {
            binding.rvShifts.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvShifts.adapter = BaseListAdapter<ShiftTemplate>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListTemplatesBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { template, binding, _ ->
                    (binding as ListTemplatesBinding).template = template
                },
                { template, _ ->
                    findNavController().navigate(
                        UnassignedFragmentDirections.navigateToShiftTemplateFragment(template)
                    )
                },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        sharedVM.signUpSuccess.observe(this, successObserver)
        sharedVM.removeSuccess.observe(this, removeObserver)

        viewModel.shifts.observe(viewLifecycleOwner, observer)

        binding.rvShifts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                binding.unassignedToolbar.appBarLayout.elevation =
                    if (recyclerView.computeVerticalScrollOffset() > 0) 6F else 0F
            }
        })
    }

    companion object {
        private const val CONSUMER_TAG = "UnassignedFragmentConsumer"
    }
}
