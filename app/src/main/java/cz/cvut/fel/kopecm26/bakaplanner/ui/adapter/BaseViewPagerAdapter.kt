package cz.cvut.fel.kopecm26.bakaplanner.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class BaseViewPagerAdapter(
    activity: FragmentManager,
    lifecycle: Lifecycle,
    private val items: List<Fragment>,
    private val toItem: (position: Int) -> Fragment = { items[it] }
) : FragmentStateAdapter(activity, lifecycle) {

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment = toItem.invoke(position)
}
