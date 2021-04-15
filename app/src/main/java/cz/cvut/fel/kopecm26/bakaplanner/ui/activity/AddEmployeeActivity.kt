package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.appcompat.widget.Toolbar
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityAddEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AddEmployeeViewModel

class AddEmployeeActivity : ViewModelActivity<AddEmployeeViewModel, ActivityAddEmployeeBinding>(
    R.layout.activity_add_employee,
    AddEmployeeViewModel::class
) {
    override val toolbar: Toolbar get() = binding.mainToolbar.toolbar
    override val navigateUp = true
}
