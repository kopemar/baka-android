package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.organization

import android.app.Activity
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentOrganizationSpecializationsBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListSpecializationBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.Specialization
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.AddSpecializationActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.OrganizationSpecializationsViewModel

class OrganizationSpecializationsFragment :
    ViewModelFragment<OrganizationSpecializationsViewModel, FragmentOrganizationSpecializationsBinding>(
        R.layout.fragment_organization_specializations, OrganizationSpecializationsViewModel::class
    ) {
    override val toolbar: Toolbar get() = binding.mainToolbar.toolbar
    override var navigateUp = true

    private val observer by lazy {
        Observer<List<Specialization>> {
            binding.rvEmployees.adapter = BaseListAdapter<Specialization>(
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
                null,
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    override fun initUi() {
        viewModel.specializations.observe(viewLifecycleOwner, observer)

        binding.fab.setOnClickListener {
            startActivityForResult<AddSpecializationActivity>(ADD_SPECIALIZATION_RC)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_SPECIALIZATION_RC && resultCode == Activity.RESULT_OK) {
            viewModel.fetchOrganizationSpecializations()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        private const val ADD_SPECIALIZATION_RC = 1118
    }
}