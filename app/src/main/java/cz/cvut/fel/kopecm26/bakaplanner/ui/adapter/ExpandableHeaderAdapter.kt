package cz.cvut.fel.kopecm26.bakaplanner.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import cz.cvut.fel.kopecm26.bakaplanner.databinding.HeaderExpandableBinding

// https://medium.com/swlh/expandable-list-in-android-with-mergeadapter-3a7f0cb56166
// https://medium.com/codeshake/create-an-expandable-recyclerview-with-the-mergeadapter-254fd671fa5b
class ExpandableHeaderAdapter<S, T>(
    private val inflateHeader: (layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> ViewDataBinding,
    private val bindHeader: (item: S, binding: ViewDataBinding, index: Int) -> Unit,
    inflate: (layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> ViewDataBinding,
    bind: (item: T, binding: ViewBinding, index: Int) -> Unit,
    onClick: ((item: T, binding: ViewBinding) -> Unit)? = null,
    compareItems: (old: T, new: T) -> Boolean,
    compareContents: (old: T, new: T) -> Boolean
) : BaseListAdapter<T>(inflate, bind, onClick, compareItems, compareContents) {

    var header: S? = null

    private val onHeaderClickListener = View.OnClickListener {
        isExpanded = !isExpanded
    }

    var isExpanded: Boolean = false
        set(value) {
            if (value) {
                notifyItemRangeInserted(1, items.size)
            } else {
                notifyItemRangeRemoved(1, items.size)
            }
            notifyItemChanged(0)
            field = value
        }


    override fun getItemViewType(position: Int): Int {
        return if (position == 0) VIEW_TYPE_HEADER else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int = if (isExpanded) items.size + 1 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        if (viewType == VIEW_TYPE_HEADER) {
            HeaderViewHolder(inflateHeader(LayoutInflater.from(parent.context), parent, false))
        } else {
            super.onCreateViewHolder(parent, viewType)
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            header?.let {
                bindHeader(it, (holder as BaseListAdapter<*>.ItemViewHolder).binding, position)
            }
        } else {
            bind(
                getItem(position - 1),
                (holder as BaseListAdapter<*>.ItemViewHolder).binding,
                position
            )
        }
    }

    inner class HeaderViewHolder(binding: ViewDataBinding) :
        BaseListAdapter<T>.ItemViewHolder(binding) {
        init {
            if (binding is HeaderExpandableBinding) {
                binding.expanded = isExpanded
            }
            binding.root.setOnClickListener(onHeaderClickListener)
        }
    }

    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_HEADER = 2
    }
}