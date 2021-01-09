package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.unit

import androidx.lifecycle.ViewModelStoreOwner
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentTemplateFormBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.shared.TemplateFormViewModel

class TemplateFormFragment : ViewModelFragment<TemplateFormViewModel, FragmentTemplateFormBinding>(R.layout.fragment_template_form, TemplateFormViewModel::class) {
    override val viewModelOwner: ViewModelStoreOwner?
        get() = activity

}