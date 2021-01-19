package cz.cvut.fel.kopecm26.bakaplanner.ui.activity.base

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.NavigationRes
import androidx.databinding.ViewDataBinding
import cz.cvut.fel.kopecm26.bakaplanner.viewmodel.BaseViewModel
import kotlin.reflect.KClass

abstract class NavigationActivity<V : BaseViewModel, B : ViewDataBinding>(
    @LayoutRes layout: Int,
    clazz: KClass<V>,
    @IdRes val navigationView: Int,
    @NavigationRes val navigation: Int
) : ViewModelActivity<V, B>(layout, clazz) {

}