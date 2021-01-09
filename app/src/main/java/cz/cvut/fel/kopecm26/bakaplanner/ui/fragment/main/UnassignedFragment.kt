package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.main

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.vvalidator.util.hide
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentUnassignedBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListShiftBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Shift
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.UnassignedViewModel
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.SignUpToShiftViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UnassignedFragment : ViewModelFragment<UnassignedViewModel, FragmentUnassignedBinding>(
    R.layout.fragment_unassigned,
    UnassignedViewModel::class
) {

    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    private val sharedVM: SignUpToShiftViewModel by sharedViewModel()

    private val successObserver by lazy {
        Observer<Boolean?> {
            if (it == true) {
                sharedVM.success.value = null
                viewModel.fetchShifts()
                showSnackBar(R.string.successfully_signed_up).apply {
                    this.setAction(R.string.ok) { snack -> snack.hide() }
                }
            }
        }
    }

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
                { findNavController().navigate(UnassignedFragmentDirections.navigateToShiftDetail(it)) },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        sharedVM.success.observe(this, successObserver)

        viewModel.shifts.observe(viewLifecycleOwner, observer)

        binding.rvShifts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Logger.d("Scroll listener ${recyclerView.computeVerticalScrollOffset()}")

                binding.unassignedToolbar.appBarLayout.elevation = if (recyclerView.computeVerticalScrollOffset() > 0) 6F else 0F
            }
        })
    }
}