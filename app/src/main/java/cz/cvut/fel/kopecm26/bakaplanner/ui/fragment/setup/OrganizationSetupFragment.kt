package cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.setup

import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.afollestad.vvalidator.form
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.FragmentOrganizationSetupBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.response.CreateOrganizationResponse
import cz.cvut.fel.kopecm26.bakaplanner.ui.fragment.base.ViewModelFragment
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.NewOrganizationViewModel

class OrganizationSetupFragment :
    ViewModelFragment<NewOrganizationViewModel, FragmentOrganizationSetupBinding>(
        R.layout.fragment_organization_setup, NewOrganizationViewModel::class
    ) {
    override val viewModel: NewOrganizationViewModel by navGraphViewModels(R.id.new_organization)

    override val toolbar: Toolbar get() = binding.setupToolbar.toolbar
    override var navigateUp = true

    override val onNavigateUp = {
        if (viewModel.working.value != true) {
            super.onNavigateUp()
        }
    }

    private val observer by lazy {
        Observer<CreateOrganizationResponse> {
            if (it.success) {
                findNavController().popBackStack(R.id.loginFragment, false)
            }
        }
    }

    override fun initUi() {
        viewModel.working.observe(viewLifecycleOwner) {
            if (it) requireActivity().hideKeyboard()
        }

        viewModel.response.observe(viewLifecycleOwner, observer)
        toolbar.inflateMenu(R.menu.check)

        setupForm()

        binding.clRoot.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                requireActivity().hideKeyboard()
            }
        }
    }

    private fun setupForm() {
        form {
            inputLayout(binding.inputFirstName) {
                isNotEmpty().description(R.string.please_insert_first_name)
            }

            inputLayout(binding.inputLastName) {
                isNotEmpty().description(R.string.please_insert_last_name)
            }

            inputLayout(binding.inputEmail) {
                isEmail().description(R.string.submit_valid_email)
            }

            inputLayout(binding.inputUsername) {
                isNotEmpty().description(R.string.username_has_to_be_filled)
                matches("[A-z0-9]+([A-z0-9.]+)+([^.])").description(R.string.username_cannot_end_with_dot)
                matches("[A-z0-9]+([A-z0-9.]).([A-z0-9]+)+").description(R.string.username_can_contain)
                matches("[a-z0-9]+([a-z0-9.]).([a-z0-9]+)+").description(R.string.username_must_be_lowercase)
            }

            inputLayout(binding.inputPassword) {
                assert(getString(R.string.must_be_at_least_6)) {
                    it.editText?.text.toString().length >= 6
                }
                matches("[A-z0-9]+").description(R.string.password_can_contain_letters_and_digits)
            }

            inputLayout(binding.inputPasswordRepeat) {
                assert(getString(R.string.passwords_must_match)) {
                    it.editText?.text.toString() == binding.inputPassword.editText?.text.toString()
                }
            }

            submitWith(toolbar.menu, R.id.action_check) {
                viewModel.submit()
            }
        }
    }
}
