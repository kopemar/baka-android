package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.unit

import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.navArgs
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentTemplatesBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.AddShiftTemplateActivity
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.TemplateViewModel

class TemplatesFragment : ViewModelFragment<TemplateViewModel, FragmentTemplatesBinding>(
    R.layout.fragment_templates,
    TemplateViewModel::class
) {
    override val toolbar: Toolbar
        get() = binding.templatesToolbar.toolbar

    override var navigateUp = true

    private val args by navArgs<TemplatesFragmentArgs>()

    override fun initUi() {
        viewModel.unit.value = args.unit
        setupMenu()
    }

    private fun setupMenu() {
        toolbar.inflateMenu(R.menu.unit_add)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_add) {
                startActivityForResult<AddShiftTemplateActivity>(ADD_TEMPLATE_RC) {
                    putSerializable(AddShiftTemplateActivity.SCHEDULING_UNIT, viewModel.unit.value)
                    this
                }
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ADD_TEMPLATE_RC) {
            viewModel.fetchTemplates()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val ADD_TEMPLATE_RC = 1111
    }
}