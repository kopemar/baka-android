package cz.cvut.fel.kopecm26.bakaplanner.util.ext

import android.text.Editable
import android.util.Patterns
import android.webkit.URLUtil

fun String.isUrl() = URLUtil.isNetworkUrl(this) && Patterns.WEB_URL.matcher(this).matches()

fun Editable.isUrl() = this.toString().isUrl()