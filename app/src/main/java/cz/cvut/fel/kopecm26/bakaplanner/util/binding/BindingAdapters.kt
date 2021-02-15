package cz.cvut.fel.kopecm26.bakaplanner.util.binding

import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

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

@BindingAdapter("android:src")
fun ImageView.src(@DrawableRes res: Int) {
    this.setImageResource(res)
}

@BindingAdapter("isRefreshing")
fun SwipeRefreshLayout.isRefreshing(boolean: Boolean) {
    isRefreshing = boolean
}

@BindingAdapter("onRefresh")
fun SwipeRefreshLayout.onRefresh(action: () -> Unit) {
    this.setOnRefreshListener { action.invoke() }
}

@BindingAdapter("drawableTint")
fun ImageView.setDrawableTint(@ColorRes color: Int) {
    setColorFilter(color)
}

@BindingAdapter("viewEnabled")
fun View.setViewEnabled(value: Boolean) {
    this.isEnabled = value
    if (!value) this.alpha = 0.36F
}