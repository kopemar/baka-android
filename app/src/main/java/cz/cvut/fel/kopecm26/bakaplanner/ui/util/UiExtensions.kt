package cz.cvut.fel.kopecm26.bakaplanner.ui.util

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import androidx.recyclerview.widget.RecyclerView
import cz.cvut.fel.kopecm26.bakaplanner.R
import cz.cvut.fel.kopecm26.bakaplanner.ui.adapter.array.BaseArrayAdapter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

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

fun Context.showDatePicker(
    year: Int? = null,
    month: Int? = null,
    day: Int? = null,
    showYearFirst: Boolean = true,
    maxYearsAgo: Long? = 15,
    onPicked: (LocalDate) -> Unit
) {
    val defaultDate = LocalDate.of(1990, 1, 1)
    DatePickerDialog(
        this,
        R.style.AlertDialogTheme,
        { _, y, m, d ->
            val dateTime = LocalDate.of(y, m + 1, d)
            onPicked(dateTime)
        },
        year ?: defaultDate.year,
        month ?: defaultDate.monthValue - 1,
        day ?: defaultDate.dayOfMonth,
    ).apply {
        // This is StackOverflow workaround to display year picker first…
        if (showYearFirst) this.datePicker.touchables.firstOrNull()?.performClick()
        if (maxYearsAgo != null) {
            this.datePicker.maxDate =
                LocalDateTime.now().minusYears(maxYearsAgo).toEpochSecond(ZoneOffset.UTC) * 1000
        }
    }.show()
}
