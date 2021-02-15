package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
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
fun <T: View> T.onClickOrFocus(action: T.() -> Unit) {
    setOnClickListener { action.invoke(this) }

    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) action.invoke(this)
    }
}