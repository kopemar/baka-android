package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.employees

import android.view.Menu
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Form
import com.afollestad.vvalidator.form.FormResult
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentAddEmployeeBinding
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.ui.util.onClickOrFocus
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.AddEmployeeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddEmployeeFormFragment: ViewModelFragment<AddEmployeeViewModel, FragmentAddEmployeeBinding>(
    R.layout.fragment_add_employee,
    AddEmployeeViewModel::class
) {
    override val viewModel: AddEmployeeViewModel by sharedViewModel()

    fun setupFormValidation(menu: Menu, menuItem: Int, onSubmit: (FormResult) -> Unit) {
        form {
            setUpFormFields()
            submitWith(menu, menuItem, onSubmit)
        }
    }

    override fun initUi() {
        binding.actvBirthDate.onClickOrFocus {
            Logger.d("birth date click")
            setUpDatePicker {
                viewModel.setDate(it)
            }
        }
    }

    private fun Form.setUpFormFields() {
        inputLayout(binding.inputFirstName) {
            isNotEmpty().description(R.string.please_insert_first_name)
        }

        inputLayout(binding.inputLastName) {
            isNotEmpty().description(R.string.please_insert_last_name)
        }

        inputLayout(binding.inputBirthDate) {
            isNotEmpty()
        }

        inputLayout(binding.inputEmail) {
            isEmail().description(R.string.submit_valid_email)
        }

        inputLayout(binding.inputUsername) {
            matches("[A-z0-9]+([A-z0-9.]+)+([^.])").description(R.string.username_cannot_end_with_dot)
            matches("[A-z0-9]+([A-z0-9.]).([A-z0-9]+)+").description(R.string.username_can_contain)
            matches("[a-z0-9]+([a-z0-9.]+)+([a-z0-9]+)+").description(R.string.username_must_be_lowercase)
        }

        inputLayout(binding.inputPassword) {
            assert(getString(R.string.must_be_at_least_6)) {
                it.editText?.text.toString().length > 6
            }
            matches("[A-z0-9]+").description(R.string.password_can_contain_letters_and_digits)
        }

        inputLayout(binding.inputPasswordRepeat) {
            assert(getString(R.string.passwords_must_match)) {
                it.editText?.text.toString() == binding.inputPassword.editText?.text.toString()
            }
        }
    }
}
