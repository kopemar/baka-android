package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.unit

import android.app.Activity
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentPlanDayBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ListTemplatesBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ShiftTemplate
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.AddShiftTemplateActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.BaseListAdapter
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.TemplatesViewModel

class TemplatesFragment : ViewModelFragment<TemplatesViewModel, FragmentPlanDayBinding>(
    R.layout.fragment_plan_day,
    TemplatesViewModel::class
) {
    override val viewModelOwner: ViewModelStoreOwner? get() = activity

    override val toolbar: Toolbar
        get() = binding.templatesToolbar.toolbar
    override var navigateUp = true

    private val observer by lazy {
        Observer<List<ShiftTemplate>> {
            binding.rvTemplates.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvTemplates.adapter = BaseListAdapter<ShiftTemplate>(
                { layoutInflater, viewGroup, attachToRoot ->
                    ListTemplatesBinding.inflate(
                        layoutInflater,
                        viewGroup,
                        attachToRoot
                    )
                },
                { template, binding, _ -> (binding as ListTemplatesBinding).template = template },
                {
                    findNavController().navigate(
                        TemplatesFragmentDirections.navigateToTemplateFragment(
                            it
                        )
                    )
                },
                { old, new -> old.id == new.id },
                { old, new -> old == new }
            ).apply { setItems(it) }
        }
    }

    private val args by navArgs<TemplatesFragmentArgs>()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_TEMPLATE_RC && resultCode == Activity.RESULT_OK) {
            viewModel.fetchTemplates()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initUi() {
        viewModel.templates.observe(viewLifecycleOwner, observer)
        viewModel.setUnit(args.unit)
        setupMenu()

        setButtons()
    }

    private fun setupMenu() {
        toolbar.inflateMenu(R.menu.unit_add)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_add) startAddShiftActivity()
            true
        }
    }

    private fun setButtons() {
        binding.emptyDayMessage.btnPlanShift.setOnClickListener {
            startAddShiftActivity()
        }
    }

    private fun startAddShiftActivity() {
        startActivityForResult<AddShiftTemplateActivity>(ADD_TEMPLATE_RC) {
            putSerializable(AddShiftTemplateActivity.SCHEDULING_UNIT, viewModel.unit.value)
            this
        }
    }

    companion object {
        const val ADD_TEMPLATE_RC = 1111
    }
}