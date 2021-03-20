package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.array.BaseArrayAdapter

fun AutoCompleteTextView.setupAdapter(
    adapter: BaseArrayAdapter<*, *>,
    listener: AdapterView.OnItemClickListener
) {
    setAdapter(adapter)
    onItemClickListener = listener
}

/**
 * Sets both click and focus listener with the same function
 */
fun <T : View> T.onClickOrFocus(action: T.() -> Unit) {
    setOnClickListener { action.invoke(this) }

    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) action.invoke(this)
    }
}

fun View.elevationOnScroll(recycler: RecyclerView) {
    recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            this@elevationOnScroll.elevation =
                if (recyclerView.computeVerticalScrollOffset() > 0) 6F else 0F
        }
    })
}

fun View.elevationOnScroll(scrollView: NestedScrollView) {
    scrollView.setOnDragListener { v, _ ->
        this@elevationOnScroll.elevation =
            if (v.scrollY > 0) 6F else 0F
        true
    }
//    scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
//
//    }
}

