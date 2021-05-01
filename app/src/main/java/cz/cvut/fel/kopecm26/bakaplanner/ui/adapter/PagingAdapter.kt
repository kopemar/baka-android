package cz.cvut.fel.kopecm26.bakaplanner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

open class PagingAdapter<T: Any>(
    protected val inflate: (layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> ViewDataBinding,
    protected val bind: (item: T, binding: ViewDataBinding, index: Int) -> Unit,
    protected val onClick: ((item: T, binding: ViewDataBinding) -> Unit)? = null,
    compareItems: (old: T, new: T) -> Boolean,
    compareContents: (old: T, new: T) -> Boolean,
    protected val onLongPress: ((item: T, binding: ViewDataBinding) -> Unit)? = null
) : PagingDataAdapter<T, RecyclerView.ViewHolder>(DiffCallback(compareItems, compareContents)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let {
            bind(
                it,
                (holder as PagingAdapter<*>.ItemViewHolder).binding, position
            )
        }
    }

    open inner class ItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder((binding).root) {
        init {
            binding.root.setOnLongClickListener {
                getItem(bindingAdapterPosition)?.let { it1 -> onLongPress?.invoke(it1, binding) }
                onLongPress != null
            }
            binding.root.setOnClickListener { getItem(bindingAdapterPosition)?.let { it1 ->
                onClick?.invoke(
                    it1, binding)
            } }
        }
    }

    private class DiffCallback<K>(
        private val compareItems: (old: K, new: K) -> Boolean,
        private val compareContents: (old: K, new: K) -> Boolean
    ) : DiffUtil.ItemCallback<K>() {
        override fun areItemsTheSame(old: K, new: K) = compareItems(old, new)
        override fun areContentsTheSame(old: K, new: K) = compareContents(old, new)
    }
}
