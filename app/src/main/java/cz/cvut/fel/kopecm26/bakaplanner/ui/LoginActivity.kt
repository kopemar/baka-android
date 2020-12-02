package cz.cvut.fel.kopecm26.bakaplanner.ui

import androidx.lifecycle.viewModelScope
import com.afollestad.vvalidator.form
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.databinding.ActivityLoginBinding
import cz.cvut.fel.kopecm26.bakaplanner.networking.UnauthorizedException
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.PrefsUtils
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.hideKeyboard
import cz.cvut.fel.kopecm26.bakaplanner.util.ext.startActivity
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginActivity: ViewModelActivity<LoginViewModel, ActivityLoginBinding>(R.layout.activity_login, LoginViewModel::class) {

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
                viewModel.viewModelScope.launch {
                    try {
                        viewModel.signIn()?.run {
                            PrefsUtils.saveUser(this)
                            finishAffinity()
                            startActivity<MainActivity>()
                        }
                    } catch (e: Exception) {
                        if (e is UnauthorizedException) showSnackBar(R.string.wrong_password)
                        else showSnackBar(R.string.unknown_error)
                    }
                }
            }
        }
    }
}