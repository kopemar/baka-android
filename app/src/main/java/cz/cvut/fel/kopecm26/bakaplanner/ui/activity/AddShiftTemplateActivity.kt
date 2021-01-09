package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import android.app.Activity
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityTemplateBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.SchedulingUnit
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.TemplateFormViewModel

class AddShiftTemplateActivity:
    ViewModelActivity<TemplateFormViewModel, ActivityTemplateBinding>(R.layout.activity_template, TemplateFormViewModel::class) {

    override val toolbar: Toolbar get() = binding.toolbar.toolbar
    override val navigateUp = true

    private val observer by lazy {
        Observer<Boolean> {
            if (it) {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    override fun initUi() {
        viewModel.success.observe(this, observer)
        viewModel.unit.value = intent?.extras?.getSerializable(SCHEDULING_UNIT) as? SchedulingUnit
    }

    override fun onSupportNavigateUp(): Boolean {
        showCancelDialog()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        showCancelDialog()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.check, menu)
        setupMenu()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupMenu() {
        Logger.d("setupMenu")

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_check) {
                viewModel.submitTemplate()
            }
            true
        }
    }

    private fun showCancelDialog() {
        showMaterialDialog(
            R.string.are_you_sure_you_want_to_cancel,
            positive = R.string.yes,
            negative = R.string.no,
            onPositive = {
                setResult(Activity.RESULT_CANCELED)
                finish()
            },
            onNegative = { it.dismiss() }
        )
    }

    companion object {
        const val SCHEDULING_UNIT = "SCHEDULING_UNIT"
    }
}