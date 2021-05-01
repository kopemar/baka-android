package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PagingScrollListener(val onScrolledDown: () -> Unit) : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
        val visibleItemCount = layoutManager?.childCount ?: 0
        val totalItemCount = layoutManager?.itemCount ?: 0
        val firstVisibleItemPosition = layoutManager?.findFirstVisibleItemPosition() ?: 0

        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) onScrolledDown()
    }
}