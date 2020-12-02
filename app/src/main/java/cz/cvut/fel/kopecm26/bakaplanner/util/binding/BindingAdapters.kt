package cz.cvut.fel.kopecm26.bakaplanner.util.binding

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("fromHtml")
fun TextView.fromHtml(html: String) {
    this.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
}