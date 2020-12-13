package cz.cvut.fel.kopecm26.bakaplanner.navigation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator

// initial src: https://github.com/STAR-ZERO/navigation-keep-fragment-sample
@Navigator.Name("keep_state_fragment")
class KeepStateNavigator(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {
        val tag = destination.id.toString()
        val transaction = manager.beginTransaction()

        var initialNavigate = false

        manager.primaryNavigationFragment?.let {
            transaction.hide(it)
        } ?: run {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)

        fragment?.let {
            transaction.show(it)
        } ?: run {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className).apply {
                transaction.add(containerId, this, tag)
            }
        }

        transaction.setPrimaryNavigationFragment(fragment)
        transaction.setReorderingAllowed(true)
        transaction.commitNow()

        return if (initialNavigate) destination else null
    }
}
