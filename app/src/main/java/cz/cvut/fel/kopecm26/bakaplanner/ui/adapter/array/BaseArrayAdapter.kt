package cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.array

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

class BaseArrayAdapter<T, V : ViewDataBinding>(
    context: Context,
    @LayoutRes val layout: Int,
    val bind: (item: T, binding: V, index: Int) -> Unit,
    val items: List<T>,
): ArrayAdapter<T>(context, layout) {
    override fun getCount() = items.size

    override fun getItem(position: Int) = items[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: V =
            DataBindingUtil.inflate(layoutInflater, layout, null, false)

        bind.invoke(getItem(position), binding, position)
        return binding.root
    }
}