package cz.cvut.fel.kopecm26.bakaplanner.ui.activity

import androidx.lifecycle.viewModelScope
import com.afollestad.vvalidator.form
import com.orhanobut.logger.Logger
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityLoginBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.model.ResponseModel
import cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base.ViewModelActivity
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.startActivity
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.toJson
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginActivity : ViewModelActivity<LoginViewModel, ActivityLoginBinding>(
    R.layout.activity_login,
    LoginViewModel::class
) {

    override val statusBarTransparent = true

    override fun initUi() {
        form {
            input(binding.etUsername.id) {
                assert(getString(R.string.username_must_be_filled)) {
                    it.text.isNotBlank()
                }
            }

            input(binding.etPassword.id) {
                assert(getString(R.string.password_must_be_filled)) {
                    it.text.isNotBlank()
                }
            }

            submitWith(binding.btnLogIn.id) {
                hideKeyboard()
                viewModel.viewModelScope.launch { signIn() }
            }
        }
    }

    private suspend fun signIn() {
        viewModel.signIn()?.run {
            if (this is ResponseModel.SUCCESS) {
                PrefsUtils.saveUser(this.data)
                Logger.d(data.toJson())
                finishAffinity()
                startActivity<MainActivity>()
            } else if (this is ResponseModel.ERROR) {
                this.errorType?.messageRes?.let { error -> showSnackBar(error) }
            }
        }
    }
}