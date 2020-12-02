package cz.cvut.fel.kopecm26.bakaplanner.util.binding

import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Sets content of [TextView] to HTML.
 */
@BindingAdapter("fromHtml")
fun TextView.fromHtml(html: String) {
    this.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
}

/**
 * Simplifies [View.setVisibility] if only [View.GONE] and [View.VISIBLE] are relevant.
 */
@BindingAdapter("visible")
fun View.visible(value: Boolean) {
    visibility = if (value) View.VISIBLE else View.GONE
}