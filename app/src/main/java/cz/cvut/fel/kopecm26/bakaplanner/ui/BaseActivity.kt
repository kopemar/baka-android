package cz.cvut.fel.kopecm26.bakaplanner.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<B : ViewDataBinding>(private val layoutRes: Int) : AppCompatActivity() {

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initUi()
    }

    open fun initUi() {}

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, layoutRes)
    }
}