package cz.cvut.fel.kopecm26.bakaplanner.ui.util

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