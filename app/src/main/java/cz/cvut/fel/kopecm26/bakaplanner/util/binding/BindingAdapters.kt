package cz.cvut.fel.kopecm26.bakaplanner.util.binding

import android.text.Html
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.orhanobut.logger.Logger

@BindingAdapter("fromHtml")
fun TextView.fromHtml(html: String) {
    Logger.d(html)
    this.text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
}