package cz.cvut.fel.kopecm26.bakaplanner.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

// https://medium.com/swlh/expandable-list-in-android-with-mergeadapter-3a7f0cb56166
// https://medium.com/codeshake/create-an-expandable-recyclerview-with-the-mergeadapter-254fd671fa5b
class WrappedAdapter<S, T, B>(
    private val inflateHeader: ((layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> ViewDataBinding)? = null,
    private val bindHeader: ((item: S, binding: ViewDataBinding, index: Int) -> Unit)? = null,
    inflate: (layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> ViewDataBinding,
    bind: (item: T, binding: ViewBinding, index: Int) -> Unit,
    onClick: ((item: T, binding: ViewBinding) -> Unit)? = null,
    compareItems: (old: T, new: T) -> Boolean,
    compareContents: (old: T, new: T) -> Boolean,
    private val onHeaderClick: (() -> Unit)? = null,
    private val onFooterClick: (() -> Unit)? = null,
    private val inflateBottom: ((layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> ViewDataBinding)? = null,
    private val bindFooter: ((item: B, binding: ViewDataBinding, index: Int) -> Unit)? = null,
) : BaseListAdapter<T>(inflate, bind, onClick, compareItems, compareContents) {

    var header: S? = null
    var footer: B? = null

    private val onHeaderClickListener = View.OnClickListener {
        onHeaderClick?.invoke()
    }

    private val onFooterClickListener = View.OnClickListener {
        onFooterClick?.invoke()
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            // if header is present ->
            inflateHeader != null -> when (position) {
                0 -> VIEW_TYPE_HEADER
                items.size + 1 -> VIEW_TYPE_FOOTER
                else -> VIEW_TYPE_ITEM
            }
            position == items.size -> VIEW_TYPE_FOOTER
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun getItemCount(): Int = items.size + (inflateHeader?.let { 1 } ?: 0) + (inflateBottom?.let { 1 } ?: 0)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        if (viewType == VIEW_TYPE_HEADER && inflateHeader != null) {
            HeaderViewHolder(inflateHeader.invoke(LayoutInflater.from(parent.context), parent, false))
        } else {
            WrappedViewHolder(inflate.invoke(LayoutInflater.from(parent.context), parent, false))
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && inflateHeader != null) {
            header?.let {
                bindHeader?.invoke(it, (holder as BaseListAdapter<*>.ItemViewHolder).binding, position)
            }
        }
        else if ((position == items.size && inflateHeader == null) || position == items.size + 1) {
            footer?.let {
                bindFooter?.invoke(it, (holder as BaseListAdapter<*>.ItemViewHolder).binding, position)
            }
        } else {
            bind(
                getItem(position - (inflateHeader?.let { 1 } ?: 0)),
                (holder as BaseListAdapter<*>.ItemViewHolder).binding,
                position
            )
        }
    }

    open inner class WrappedViewHolder(binding: ViewDataBinding) : BaseListAdapter<T>.ItemViewHolder(binding) {
        init {
            binding.root.setOnLongClickListener {
                onLongPress?.invoke(getItem(bindingAdapterPosition - (inflateHeader?.let { 1 } ?: 0)), binding)
                onLongPress != null
            }
            binding.root.setOnClickListener { onClick?.invoke(getItem(bindingAdapterPosition - (inflateHeader?.let { 1 } ?: 0)), binding) }
        }
    }

    inner class HeaderViewHolder(binding: ViewDataBinding) :
        BaseListAdapter<T>.ItemViewHolder(binding) {
        init {
            binding.root.setOnClickListener(onHeaderClickListener)
        }
    }

    inner class FooterViewHolder(binding: ViewDataBinding) :
        BaseListAdapter<T>.ItemViewHolder(binding) {
        init {
            binding.root.setOnClickListener(onFooterClickListener)
        }
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_HEADER = 2
        private const val VIEW_TYPE_FOOTER = 3
    }
}
