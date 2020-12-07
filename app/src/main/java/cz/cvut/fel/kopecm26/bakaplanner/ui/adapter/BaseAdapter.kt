import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseListAdapter<T>(
    private val inflate: (layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean) -> ViewBinding,
    private val bind: (item: T, binding: ViewBinding, index: Int) -> Unit,
    private val onClick: ((item: T) -> Unit)? = null,
    compareItems: (old: T, new: T) -> Boolean,
    compareContents: (old: T, new: T) -> Boolean
) : ListAdapter<T, RecyclerView.ViewHolder>(DiffCallback(compareItems, compareContents)) {
    var items = emptyList<T>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        bind(getItem(position), (holder as BaseListAdapter<T>.ItemViewHolder).binding, position)
    }

    internal fun setItems(items: List<T>) {
        this.items = items
        this.submitList(items)
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder((binding).root) {
        init {
            binding.root.setOnClickListener { onClick?.invoke(getItem(adapterPosition)) }
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