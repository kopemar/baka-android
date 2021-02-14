package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.appcompat.widget.Toolbar
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityWeekBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.BindingActivity

class PlanWeekActivity: BindingActivity<ActivityWeekBinding>(R.layout.activity_week) {

    override val toolbar: Toolbar get() = binding.toolbar.toolbar
    override val navigateUp = true
    override val navigateUpRes: Int get() = R.drawable.ic_mdi_close

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    override fun initUi() {
        toolbar.setTitle(R.string.plan_week)
    }
}