package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityTemplateBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.TemplateFormViewModel

class AddShiftTemplateActivity:
    ViewModelActivity<TemplateFormViewModel, ActivityTemplateBinding>(R.layout.activity_template, TemplateFormViewModel::class) {

    override fun initUi() {
        super.initUi()
        viewModel.unit.value = intent?.extras?.getSerializable(SCHEDULING_UNIT) as? SchedulingUnit
        setUpToolbar(binding.toolbar.toolbar)
    }

    override fun onSupportNavigateUp(): Boolean {
        showCancelDialog()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        showCancelDialog()
    }

    private fun showCancelDialog() {
        showMaterialDialog(
            R.string.are_you_sure_you_want_to_cancel,
            positive = R.string.yes,
            negative = R.string.no,
            onPositive = { finish() },
            onNegative = { it.dismiss() }
        )
    }

    companion object {
        const val SCHEDULING_UNIT = "SCHEDULING_UNIT"
    }
}