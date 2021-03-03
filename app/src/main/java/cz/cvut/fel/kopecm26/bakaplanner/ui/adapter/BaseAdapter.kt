package cz.cvut.fel.kopecm26.bakaplanner.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

open class BaseListAdapter<T>(
    private val inflate: (layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> ViewDataBinding,
    protected val bind: (item: T, binding: ViewDataBinding, index: Int) -> Unit,
    private val onClick: ((item: T, binding: ViewDataBinding) -> Unit)? = null,
    compareItems: (old: T, new: T) -> Boolean,
    compareContents: (old: T, new: T) -> Boolean,
    private val onLongPress: ((item: T, binding: ViewDataBinding) -> Unit)? = null
) : ListAdapter<T, RecyclerView.ViewHolder>(DiffCallback(compareItems, compareContents)) {
    var items = emptyList<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bind(
            getItem(position),
            (holder as BaseListAdapter<*>.ItemViewHolder).binding, position
        )
    }

    internal fun setItems(items: List<T>) {
        this.items = items
        this.submitList(items)
    }

    override fun getItemCount(): Int = items.size

    open inner class ItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder((binding).root) {
        init {
            binding.root.setOnLongClickListener {
                onLongPress?.invoke(getItem(bindingAdapterPosition), binding)
                onLongPress != null
            }
            binding.root.setOnClickListener { onClick?.invoke(getItem(bindingAdapterPosition), binding) }
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
